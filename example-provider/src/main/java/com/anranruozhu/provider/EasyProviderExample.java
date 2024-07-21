package com.anranruozhu.provider;

import com.anranruozhu.server.HttpServer;
import com.anranruozhu.server.VertxHttpServer;

/**
 * @author anranruozhu
 * @className EasyProviderExample
 * @description 简易服务提供者示例
 * @create 2024/7/21 下午4:18
 **/
public class EasyProviderExample {
    public static void main(String[] args) {
        //提供服务
        HttpServer httpServer=new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
