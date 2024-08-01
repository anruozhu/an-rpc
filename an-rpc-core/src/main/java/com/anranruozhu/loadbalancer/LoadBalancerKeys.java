package com.anranruozhu.loadbalancer;

/**
 * @author anranruozhu
 * @className LoadBalancerKeys
 * @description 负载均衡键名常量
 * @create 2024/8/1 下午2:15
 **/
public interface LoadBalancerKeys {
        String ROUND_ROBIN = "roundRobin";
        String RANDOM = "random";
        String CONSISTENT_HASH = "consistentHash";
}
