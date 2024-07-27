package com.anranruozhu.protocol;

import lombok.Getter;

/**
 *  协议消息状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     *  根据value获取枚举值
     *
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getNumByValue(int value) {
        for(ProtocolMessageStatusEnum e : ProtocolMessageStatusEnum.values()) {
            if(e.value == value) {
                return e;
            }
        }
        return null;
    }
}
