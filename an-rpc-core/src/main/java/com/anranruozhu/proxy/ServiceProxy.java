package com.anranruozhu.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anranruozhu.RpcApplication;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.model.ServiceMetaInfo;
import com.anranruozhu.protocol.*;
import com.anranruozhu.registry.Registry;
import com.anranruozhu.registry.RegistryFactory;
import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author anranruozhu
 * @className ServiceProxy
 * @description 服务代理(JDK动态代理)
 * @create 2024/7/23 上午9:00
 **/
public class ServiceProxy implements InvocationHandler {


    final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        //指定序列化器

        RPCRequest rpcRequest = RPCRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(params)
                .build();
        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            //从注册中心获取服务器提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //暂时先取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            //发送请求 TCP 请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RPCResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            System.out.println("Connected to TCP server");
                            NetSocket socket = result.result();
                            //发送消息
                            //构造消息
                            ProtocolMessage<RPCRequest> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);
                            //编码请求
                            try {
                                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encodeBuffer);

                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }
                            //接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RPCResponse> responseProtocolMessage = (ProtocolMessage<RPCResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(responseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议解码错误");
                                }
                            });
                        } else {
                            System.out.println("Failed to connect to TCP server");
                        }
                    });
            RPCResponse rpcResponse = responseFuture.get();
            //关闭连接
            netClient.close();
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
