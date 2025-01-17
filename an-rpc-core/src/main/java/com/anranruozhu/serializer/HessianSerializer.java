package com.anranruozhu.serializer;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author anranruozhu
 * @className HessianSerializer
 * @description Hessian序列化器
 * @create 2024/7/24 上午9:50
 **/
public class HessianSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream Bos=new ByteArrayOutputStream();
        HessianOutput ho=new HessianOutput(Bos);
        ho.writeObject(obj);
        return Bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream Bis=new ByteArrayInputStream(data);
        HessianInput hi=new HessianInput(Bis);
        return (T)hi.readObject(clazz);
    }
}
