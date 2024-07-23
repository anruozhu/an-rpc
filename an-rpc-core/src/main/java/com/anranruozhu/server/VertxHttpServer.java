package com.anranruozhu.server;

import io.vertx.core.Vertx;

/**
 * @author anranruozhu
 * @className VertxHttpServer
 * @description 基于Vert.x实现的Http服务器
 * @create 2024/7/21 下午4:31
 **/
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {

        //创建 Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
        server.requestHandler(new HTTPServeHandler());
        //启动 HTTP 服务并监听指定端口
        server.listen(port,result-> {
            if(result.succeeded()){
                System.out.println("Vert.x HTTP server listening on port "+port);
            } else {
                System.out.println("Failed to start Vert.x HTTP server");
            }
        });
    }
}
