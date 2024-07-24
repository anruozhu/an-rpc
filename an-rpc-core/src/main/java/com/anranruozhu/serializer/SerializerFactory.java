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

    static{
        SpiLoader.load(Serializer.class);
    }

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
        return SpiLoader.getInstance(Serializer.class,Key);
    }
}
