package com.anranruozhu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author anranruozhu
 * @className RPCRequest
 * @description RPC请求
 * @create 2024/7/21 下午5:21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCRequest implements Serializable {
    /**
     *  服务名称
     */
    private String serviceName;

    /**
     *  方法名称
     */
    private String methodName;

    /**
     *  参数类型列表
     */
    private Class<?>[] parameterTypes;;

    /**
     *  参数列表
     */
    private Object[] params;
}
