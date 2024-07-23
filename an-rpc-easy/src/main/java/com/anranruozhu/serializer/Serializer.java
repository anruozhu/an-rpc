package com.anranruozhu.serializer;

import java.io.IOException;

/**
 *  序列化器接口
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}
