package com.anranruozhu.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anranruozhu.RpcApplication;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import com.anranruozhu.model.ServiceMetaInfo;
import com.anranruozhu.registry.Registry;
import com.anranruozhu.registry.RegistryFactory;
import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author anranruozhu
 * @className ServiceProxy
 * @description 服务代理(JDK动态代理)
 * @create 2024/7/23 上午9:00
 **/
public class ServiceProxy implements InvocationHandler {


    final Serializer serializer= SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
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
        // Serializer serializer=new JdkSerializer();
        RPCRequest rpcRequest=RPCRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(params)
                .build();
      try {
          //序列化
          byte[] bodyBytes=serializer.serialize(rpcRequest);
          //从注册中心获取服务器提供者请求地址
          RpcConfig rpcConfig=RpcApplication.getRpcConfig();
          Registry registry= RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
          ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
          serviceMetaInfo.setServiceName(serviceName);
          serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
          List<ServiceMetaInfo> serviceMetaInfoList=registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
          if (CollUtil.isEmpty(serviceMetaInfoList)){
              throw new RuntimeException("暂无服务地址");
          }
          //暂时先取第一个
          ServiceMetaInfo selectedServiceMetaInfo=serviceMetaInfoList.get(0);
          //发送请求
          try( HttpResponse httpResponse= HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                  .body(bodyBytes)
                  .execute()) {
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
