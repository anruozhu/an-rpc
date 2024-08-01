package com.anranruozhu.loadbalancer;

import com.anranruozhu.spi.SpiLoader;

/**
 * @author anranruozhu
 * @className LoadBalancerFactory
 * @description 负载均衡工厂（工厂模式，用于获取负载均衡器对象）
 * @create 2024/8/1 下午2:17
 **/
public class LoadBalancerFactory {

    static{
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     *  获取实例
     * @param key
     * @return
     */
    public static LoadBalancer getIntstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
