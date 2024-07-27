package com.anranruozhu.protocol;

import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author anranruozhu
 * @className ProtocolMessageEncoder
 * @description 协议信息编码器
 * @create 2024/7/27 下午5:07
 **/
public class ProtocolMessageEncoder {
    public static Buffer encode(ProtocolMessage<?> msg) throws IOException {
        if (msg==null || msg.getHeader()==null){
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = msg.getHeader();
        //依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        //获取序列化器
        ProtocolMessageSerializerEnum serializerEnum= ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if (serializerEnum==null){
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer= SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(msg.getBody());
        //写入 body 长度和数据
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
    return buffer;
    }
}
