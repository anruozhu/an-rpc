package com.anranruozhu.serializer;


import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author anranruozhu
 * @className JsonSerializer
 * @description Json序列化器
 * @create 2024/7/24 上午9:18
 **/
public class JsonSerializer implements Serializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        T obj = OBJECT_MAPPER.readValue(data, clazz);
        if (obj instanceof RPCRequest) {
            return handleRequest((RPCRequest) obj, clazz);
        }
        if (obj instanceof RPCResponse) {
            return handleResponse((RPCResponse) obj, clazz);
        }
        return null;
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时被视为LinkedHashMap 无法转换成原始对象
     * 因此进行特殊处理
     *
     * @param rpcRequest
     * @param clazz
     * @return {@link <T>}
     * @throws IOException
     */
    private <T> T handleRequest(RPCRequest rpcRequest, Class<T> clazz) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParams();
        //循环处理每个参数类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            //如果类型不同，则重新处理一下类型
            if (!parameterType.isAssignableFrom(parameters[i].getClass())) {
                byte[] paramBytes = OBJECT_MAPPER.writeValueAsBytes(parameters[i]);
                parameters[i] = OBJECT_MAPPER.readValue(paramBytes, parameterType);
            }
        }
        return clazz.cast(rpcRequest);
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时被视为LinkedHashMap 无法转换成原始对象
     * 因此进行特殊处理
     *
     * @param response
     * @param clazz
     * @return {@link <T>}
     * @throws IOException
     */
    private <T> T handleResponse(RPCResponse response, Class<T> clazz) throws IOException {
        byte[] byteData = OBJECT_MAPPER.writeValueAsBytes(response.getData());
        response.setData(OBJECT_MAPPER.readValue(byteData, response.getDataType()));
        return clazz.cast(response);
    }
}
