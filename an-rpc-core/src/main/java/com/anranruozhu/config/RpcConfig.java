package com.anranruozhu.config;

import com.anranruozhu.fault.retry.RetryStrategyKeys;
import com.anranruozhu.loadbalancer.LoadBalancerKeys;
import com.anranruozhu.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author anranruozhu
 * @className RpcConfig
 * @description RPC框架配置
 * @create 2024/7/23 上午10:55
 **/
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name="an-rpc";
    /**
     * 版本号
     */
    private String version="1.0";
    /**
     * 服务主机地址
     */
    private String serverHost="localhost";
    /**
     * 服务端口
     */
    private int serverPort=8080;

    /**
     * 模拟调用
     */
    private boolean mock=false;

    /**
     * 序列化方式
     */
    private String serializer= SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig=new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer= LoadBalancerKeys.ROUND_ROBIN;
    /**
     * 重试策略
     */
    private String retryStrategy= RetryStrategyKeys.FIXED_INTERVAL;
}
