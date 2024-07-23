package com.anranruozhu.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anranruozhu
 * @className LocalRegistry
 * @description 本地注册中心
 * @create 2024/7/21 下午5:03
 **/
public class LocalRegistry {
    /**
     *  注册信息存储
     */
    private static final Map<String,Class<?>> map=new ConcurrentHashMap<>() ;

    /**
     *  注册服务
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }

    /**
     *  获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }
}
