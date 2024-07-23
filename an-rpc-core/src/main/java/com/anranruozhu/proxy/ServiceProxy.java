package com.anranruozhu.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.serializer.JdkSerializer;
import com.anranruozhu.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author anranruozhu
 * @className ServiceProxy
 * @description 服务代理(JDK动态代理)
 * @create 2024/7/23 上午9:00
 **/
public class ServiceProxy implements InvocationHandler {


    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        //指定序列化器
        Serializer serializer=new JdkSerializer();
        RPCRequest rpcRequest=RPCRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(params)
                .build();
      try {
          //序列化
          byte[] bodyBytes=serializer.serialize(rpcRequest);
          //发送请求
          //todo 注意，这里地址被反编码了（需要使用注册中心发现机制解决）
          try( HttpResponse httpResponse= HttpRequest.post("http://localhost:8080")
                  .body(bodyBytes)
                  .execute();) {
              byte[] result=httpResponse.bodyBytes();
              //反序列化
              RPCResponse rpcResponse=serializer.deserialize(result, RPCResponse.class);
              return rpcResponse.getData();
          }
      }catch (IOException e){
          e.printStackTrace();
      }
        return null;
    }
}
