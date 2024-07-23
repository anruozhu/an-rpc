package com.anranruozhu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author anranruozhu
 * @className RPCResponse
 * @description RPC响应
 * @create 2024/7/21 下午5:21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCResponse implements Serializable {
    /**
     *  响应的数据
     */
    private Object data;

    /**
     *  响应的数据类型(预留)
     */
    private Class<?> dataType;

    /**
     *  响应的信息
     */
    private String message;

    /**
     *  响应的异常信息
     */
    private Exception exception;
}
