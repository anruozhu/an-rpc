package com.anranruozhu.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  协议消息的序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getByKey(int key) {
        for ( ProtocolMessageSerializerEnum item : ProtocolMessageSerializerEnum.values()) {
            if (item.key == key) {
                return item;
            }
        }
        return null;
    }
    public static ProtocolMessageSerializerEnum getByValue(String value) {
        //因为字符串匹配不能为null所以先排除null
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(ProtocolMessageSerializerEnum item : ProtocolMessageSerializerEnum.values()) {
            if(item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
