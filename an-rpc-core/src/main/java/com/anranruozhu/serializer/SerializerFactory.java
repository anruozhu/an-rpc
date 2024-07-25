package com.anranruozhu.serializer;

import cn.hutool.json.serialize.JSONSerializer;
import com.anranruozhu.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anranruozhu
 * @className SerializerFactory
 * @description 序列化工厂
 * @create 2024/7/24 上午9:58
 **/
public class SerializerFactory {
    private static volatile SpiLoader spiLoader;


    /**
     * 默认序列化器
     */
    private   static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param Key
     * @return
     */
    public static Serializer getInstance(String Key){
        if(spiLoader==null){
            synchronized (SpiLoader.class){
                if(spiLoader==null){
                    spiLoader = new SpiLoader();
                    SpiLoader.load(Serializer.class);
                }
            }
        }
        return SpiLoader.getInstance(Serializer.class,Key);
    }
}
