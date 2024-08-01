package com.anranruozhu.loadbalancer;

import com.anranruozhu.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author anranruozhu
 * @className LoadBalancer
 * @description 负载均衡器（消费端使用）
 * @create 2024/7/30 下午2:35
 **/
public interface LoadBalancer {


    /**
     *  选择服务调用
     *
     * @param requestParams     请求参数
     * @param serviceMetaInfoList  可用服务列表
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
