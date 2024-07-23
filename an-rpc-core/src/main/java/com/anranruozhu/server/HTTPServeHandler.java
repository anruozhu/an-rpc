package com.anranruozhu.server;


import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.registry.LocalRegistry;
import com.anranruozhu.serializer.JdkSerializer;
import com.anranruozhu.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author anranruozhu
 * @className HTTPServeHandler
 * @description HTTP请求处理器
 * @create 2024/7/21 下午5:28
 **/
public class HTTPServeHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer=new JdkSerializer();

        // 记录日志
        System.out.println("Received HTTP request:"+request.method()+" "+ request.uri());

        // 异步处理HTTP请求
        request.bodyHandler(body->{
            byte[] bytes=body.getBytes();
            RPCRequest rpcRequest=null;
            try{
                rpcRequest=serializer.deserialize(bytes,RPCRequest.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            //构造响应类型结构体
            RPCResponse rpcResponse=new RPCResponse();

            //如果请求为null就直接返回
            if (rpcRequest==null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return ;
            }

            try {
                //获取要调用的服务实现类。通过反射调用
                Class<?> implClass= LocalRegistry.get(rpcRequest.getServiceName());
                Method method= implClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
                Object result=method.invoke(implClass.newInstance(),rpcRequest.getParams());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("success");
            } catch (Exception e) {
               e.printStackTrace();
               rpcResponse.setMessage(e.getMessage());
               rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    /**
     *  进行响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void  doResponse(HttpServerRequest request,RPCResponse rpcResponse,Serializer serializer){
        HttpServerResponse httpServerResponse=request.response()
                .putHeader("content-type","application/json");
        try{
           // 序列化
            byte[] serialized=serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        }catch(IOException e){
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
