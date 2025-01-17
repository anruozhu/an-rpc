package com.anranruozhu.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anranruozhu.RpcApplication;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.fault.retry.RetryStrategy;
import com.anranruozhu.fault.retry.RetryStrategyFactory;
import com.anranruozhu.loadbalancer.LoadBalancer;
import com.anranruozhu.loadbalancer.LoadBalancerFactory;
import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.model.ServiceMetaInfo;
import com.anranruozhu.protocol.*;
import com.anranruozhu.registry.Registry;
import com.anranruozhu.registry.RegistryFactory;
import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerFactory;
import com.anranruozhu.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author anranruozhu
 * @className ServiceProxy
 * @description 服务代理(JDK动态代理)
 * @create 2024/7/23 上午9:00
 **/
public class ServiceProxy implements InvocationHandler {


    final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        //指定序列化器

        RPCRequest rpcRequest = RPCRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(params)
                .build();
        try {
            //从注册中心获取服务器提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //负载均衡
            LoadBalancer loadBalancer= LoadBalancerFactory.getIntstance(rpcConfig.getLoadBalancer());
            //将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams=new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);

            //rpc 请求
            //使用重试机制
            RetryStrategy retryStrategy= RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());

            RPCResponse rpcResponse = retryStrategy.doRetry(()->
                VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
            return rpcResponse.getData();
        }catch (Exception e){
           throw  new RuntimeException("调用失败");
        }
    }
}
