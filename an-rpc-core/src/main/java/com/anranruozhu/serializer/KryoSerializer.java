package com.anranruozhu.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author anranruozhu
 * @className KryoSerializer
 * @description Kryo序列化器
 * @create 2024/7/24 上午9:42
 **/
public class KryoSerializer implements Serializer{

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL =ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        //设置动态序列化和反序列化，不提前注册所有类（可能有安全问题）
        kryo.setRegistrationRequired(false);
        return kryo;
    });
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Output out=new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeObject(out, obj);
        out.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data);
        Input in=new Input(byteArrayInputStream);
        T result=KRYO_THREAD_LOCAL.get().readObject(in, clazz);
        in.close();
        return result;
    }
}
