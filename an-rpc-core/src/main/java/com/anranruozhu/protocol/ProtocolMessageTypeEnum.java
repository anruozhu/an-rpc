package com.anranruozhu.protocol;

import lombok.Getter;

/**
 *  协议消息类型枚举
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);


    private final int key;
    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }
    public static ProtocolMessageTypeEnum getNumByKey(int key) {
        for(ProtocolMessageTypeEnum e : ProtocolMessageTypeEnum.values()) {
            if(e.key == key) {
                return e;
            }
        }
        return null;
    }
}
