package com.anranruozhu.protocol;

/**
 * @author anranruozhu
 * @className ProtocolConstant
 * @description 协议常量
 * @create 2024/7/27 下午4:13
 **/
public interface ProtocolConstant {

    /**
     *  消息头长度
     */
    int MESSAGE_HEADER_LENGTH =17;

    /**
     *  协议魔数
     */
    byte PROTOCOL_MAGIC=0x1;

    /**
     *  协议版本号
     */
    byte PROTOCOL_VERSION=0x1;
}
