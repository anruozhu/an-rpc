package com.anranruozhu.server.tcp;

import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.protocol.*;
import com.anranruozhu.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author anranruozhu
 * @className TCPServerHandle
 * @description 请求处理器
 * @create 2024/7/27 下午5:39
 **/
public class TCPServerHandle implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper bufferHandlerWrapper=new TcpBufferHandlerWrapper(buffer->{
                //处理连接
                //接收请求，解码
                ProtocolMessage<RPCRequest> protocolMessage;
                try {
                    protocolMessage = (ProtocolMessage<RPCRequest>) ProtocolMessageDecoder.decode(buffer);
                } catch (IOException e) {
                    throw new RuntimeException("协议消息解码错误");
                }
                RPCRequest rpcRequest = protocolMessage.getBody();
                //处理请求
                //构建响应结果对象
                RPCResponse rpcResponse = new RPCResponse();
                try {
                    //获取要调用的服务实现类，通过反射调用
                    Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                    Method method;
                    method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                    Object result = method.invoke(implClass.newInstance(), rpcRequest.getParams());
                    //封装返回结果
                    rpcResponse.setData(result);
                    rpcResponse.setDataType(method.getReturnType());
                    rpcResponse.setMessage("success");
                } catch (Exception e) {
                    e.printStackTrace();
                    rpcResponse.setMessage(e.getMessage());
                    rpcResponse.setException(e);
                }
                //发送响应，编码
                ProtocolMessage.Header header = protocolMessage.getHeader();
                header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
                ProtocolMessage<RPCResponse> responseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);

                try {
                    Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                    netSocket.write(encode);
                } catch (IOException e) {
                    throw new RuntimeException("协议消息编码错误");
                }
            });
        netSocket.handler(bufferHandlerWrapper);
    }
}
