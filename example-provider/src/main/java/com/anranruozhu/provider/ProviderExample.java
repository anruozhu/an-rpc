package com.anranruozhu.provider;

import com.anranruozhu.RpcApplication;
import com.anranruozhu.common.service.UserService;
import com.anranruozhu.config.RegistryConfig;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.model.ServiceMetaInfo;
import com.anranruozhu.registry.LocalRegistry;
import com.anranruozhu.registry.Registry;
import com.anranruozhu.registry.RegistryFactory;
import com.anranruozhu.server.HttpServer;
import com.anranruozhu.server.VertxHttpServer;
import com.anranruozhu.server.tcp.VertxTcpServer;

import java.security.Provider;

/**
 * @author anranruozhu
 * @className ProviderExample
 * @description 服务提供者示例
 * @create 2024/7/25 下午5:12
 **/
public class ProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //启动TCP服
        VertxTcpServer vertxTcpServer =new VertxTcpServer();
            vertxTcpServer.doStart(8080);
        }
    }
