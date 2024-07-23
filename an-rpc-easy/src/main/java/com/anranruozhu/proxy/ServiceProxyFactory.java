package com.anranruozhu.proxy;

import java.lang.reflect.Proxy;

/**
 * @author anranruozhu
 * @className ServiceProxyFactory
 * @description 服务代理工厂(用于创建代理对象)
 * @create 2024/7/23 上午9:35
 **/
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
