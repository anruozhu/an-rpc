package com.anranruozhu.serializer;

import cn.hutool.json.serialize.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anranruozhu
 * @className SerializerFactory
 * @description 序列化工厂
 * @create 2024/7/24 上午9:58
 **/
public class SerializerFactory {

    /**
     * 序列化映射（用于实现单例）
     */
    private static final Map<String,Serializer> serializers = new HashMap<String,Serializer>(){{
        put(SerializerKeys.JDK, new JdkSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.HESSIAN,new HessianSerializer());
        put(SerializerKeys.JSON,new JsonSerializer());
    }};

    /**
     * 默认序列化器
     */
    private   static final Serializer DEFAULT_SERIALIZER = serializers.get(SerializerKeys.JDK);

    /**
     * 获取实例
     *
     * @param Key
     * @return
     */
    public static Serializer getInstance(String Key){
        return serializers.getOrDefault(Key,DEFAULT_SERIALIZER);
    }
}
