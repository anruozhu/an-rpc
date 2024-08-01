package com.anranruozhu.loadbalancer;

import com.anranruozhu.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anranruozhu
 * @className RoundRobinLoadBalancer
 * @description 轮询负载均衡器
 * @create 2024/7/30 下午2:39
 **/
public class RoundRobinLoadBalancer implements LoadBalancer{

    /**
     *  当前轮询的下标
     */
    private final AtomicInteger currentIndex=new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if( serviceMetaInfoList.isEmpty()){
            return null;
        }
        //只有一个服务时无需轮询
        int size=serviceMetaInfoList.size();
        if(size==1){
            return serviceMetaInfoList.get(0);
        }
        //取模轮询算法 getAndIncrement()以原子方式递增获取当前值
        int index=currentIndex.getAndIncrement()%size;
        return serviceMetaInfoList.get(index);
    }
}
