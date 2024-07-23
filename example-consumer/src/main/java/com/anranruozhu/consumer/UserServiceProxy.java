package com.anranruozhu.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anranruozhu.common.model.User;
import com.anranruozhu.common.service.UserService;
import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.serializer.JdkSerializer;
import com.anranruozhu.serializer.Serializer;

import java.io.IOException;

/**
 * @author anranruozhu
 * @className UserServiceProxy
 * @description 静态代理
 * @create 2024/7/21 下午5:57
 **/
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer=new JdkSerializer();

        // 发送请求
        RPCRequest request=RPCRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .params(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes =serializer.serialize(request);
            byte[] result;
            try(HttpResponse httpResponse= HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()){
                result=httpResponse.bodyBytes();
            }
            RPCResponse rpcResponse=serializer.deserialize(result,RPCResponse.class);
            return (User)rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
