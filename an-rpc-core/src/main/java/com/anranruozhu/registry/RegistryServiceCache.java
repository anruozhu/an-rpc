package com.anranruozhu.registry;

import com.anranruozhu.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anranruozhu
 * @className RegistryServiceCache
 * @description 注册中心服务本地缓存
 * @create 2024/7/26 下午3:28
 **/
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    Map<String,List<ServiceMetaInfo>> serviceCache=new ConcurrentHashMap<>();

    /**
     *  写缓存
     *
     * @param newServiceCache
     */
    void writeCache(String service,List<ServiceMetaInfo> newServiceCache) {
        if(serviceCache.containsKey(service)){
            serviceCache.get(service).addAll(newServiceCache);
        }
        serviceCache.put(service,newServiceCache);
    }

    /**
     * 读缓存
     *
     * @return
     */
    List<ServiceMetaInfo> readCache(String service){
        return serviceCache.get(service);
    }

    void clearCache(String serviceNode){
        this.serviceCache.remove(serviceNode);
    }


}
