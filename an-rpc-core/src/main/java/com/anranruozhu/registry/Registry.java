package com.anranruozhu.registry;

import com.anranruozhu.config.RegistryConfig;
import com.anranruozhu.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author anranruozhu
 * @className Registry
 * @description 注册中心
 * @create 2024/7/25 下午3:39
 **/
public interface Registry {


    /**
     *  初始化
     *
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);
    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo)throws Exception;


    /**
     * 注销服务（服务端）
     *
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     *  服务发现（获取某服务的所有节点，消费端）
     *
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destory();

    /**
     * 心跳检测（服务端）
     */
    void heartBeat();
}
