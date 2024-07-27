package com.anranruozhu.protocol;

import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author anranruozhu
 * @className ProtocolMessageDecoder
 * @description 协议消息解码器
 * @create 2024/7/27 下午5:15
 **/
public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer)throws IOException {
        //分别从指定的位置读出 Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        //校验魔数
        if(magic!=ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        //解决沾包问题，只读指定长度的数据
        byte[] bodyBytes=buffer.getBytes(17,17+header.getBodyLength());
        //解析消息体
        ProtocolMessageSerializerEnum serializerEnum=ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if(serializerEnum==null){
            throw new RuntimeException("消息的序列化协议不存在");
        }
        Serializer serializer= SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum=ProtocolMessageTypeEnum.getNumByKey(header.getType());
        if(messageTypeEnum==null){
            throw new RuntimeException("消息的类型不存在");
        }
        switch (messageTypeEnum){
            case REQUEST:
                RPCRequest request=serializer.deserialize(bodyBytes,RPCRequest.class);
                return new ProtocolMessage<>(header,request);
            case RESPONSE:
                RPCResponse response=serializer.deserialize(bodyBytes,RPCResponse.class);
                return new ProtocolMessage<>(header,response);
            case HEART_BEAT:
            case OTHER:
                throw new RuntimeException("暂不支持该消息类型");
        }




        return null;
    }
}
