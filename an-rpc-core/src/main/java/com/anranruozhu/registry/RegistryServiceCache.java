package com.anranruozhu.registry;

import com.anranruozhu.model.ServiceMetaInfo;

import java.util.List;

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
    List<ServiceMetaInfo> serviceCache;

    /**
     *  写缓存
     *
     * @param newServiceCache
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache=newServiceCache;
    }

    /**
     * 读缓存
     *
     * @return
     */
    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    void clearCache(){
        this.serviceCache.clear();
    }


}
