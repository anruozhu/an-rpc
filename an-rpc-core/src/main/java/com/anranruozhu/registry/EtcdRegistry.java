package com.anranruozhu.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.anranruozhu.config.RegistryConfig;
import com.anranruozhu.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import javax.management.StandardMBean;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author anranruozhu
 * @className EtcdReqistry
 * @description etcd注册中心
 * @create 2024/7/25 上午10:52
 **/
public class EtcdRegistry implements Registry{
    private Client client;

    private KV kvClient;


    private final String ETCD_ROOT_PATH ="/rpc/";

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private  final static Set<String> localRegisterNodeKeySet=new HashSet<>();


    @Override
    public void init(RegistryConfig registryConfig) {
    client=Client.builder()
            .endpoints(registryConfig.getAddress())
            .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
            .build();
    kvClient=client.getKVClient();
    heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建lease和kv客户端
        Lease leaseClient= client.getLeaseClient();

        //创建一个30秒的租约
        long leaseId=leaseClient.grant(30).get().getID();

        //设置要存储的键值对
        String registerKey=ETCD_ROOT_PATH+serviceMetaInfo.getServiceKey();
        ByteSequence key=ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value=ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值对与租约关联起来，并设置过期时间
        PutOption putOption=PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        //将节点信息添加至本地缓存
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey=ETCD_ROOT_PATH+serviceMetaInfo.getServiceKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //前缀搜索，结尾记得加"/"
        String searchPrefix=ETCD_ROOT_PATH+serviceKey;


        try {
            //前缀查询
            GetOption getOption=GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues=kvClient.get(
                    ByteSequence.from(searchPrefix,StandardCharsets.UTF_8),
                    getOption)
                    .get()
                    .getKvs();

            //解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                        String value=keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destory() {
        System.out.println("当前节点下线");

        //下线节点
        //遍历本节点所有key
        for (String key : localRegisterNodeKeySet){
            try {
                kvClient.delete(ByteSequence.from(key,StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key+"节点下线失败",e);
            }
        }

        //释放资源
        if(kvClient!=null) {
            kvClient.close();
        }
        if(client!=null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        // 10秒续期一次
        CronUtil.schedule("*/10 * * * * *",new Task() {
            @Override
            public void execute() {
                for(String registerKey:localRegisterNodeKeySet){
                    try {
                        List<KeyValue> keyValues=kvClient.get(ByteSequence.from(registerKey,StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        //如果该节点已过期（需要重启节点才可以重新注册）
                        if(CollUtil.isEmpty(keyValues)){
                            continue;
                        }
                        //节点未过期，重新注册=>重新续期
                        KeyValue keyValue=keyValues.get(0);
                        String value=keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo=JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);

                    } catch (Exception e) {
                        throw new RuntimeException(registerKey+"续约失败",e);
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
