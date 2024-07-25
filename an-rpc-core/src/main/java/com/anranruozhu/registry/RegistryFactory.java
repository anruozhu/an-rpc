package com.anranruozhu.registry;

import com.anranruozhu.spi.SpiLoader;


/**
 * @author anranruozhu
 * @className RegistryFactory
 * @description 注册中心工厂(用于获取注册中心队象)
 * @create 2024/7/25 下午4:05
 **/
public class RegistryFactory{
    static{
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }

}
