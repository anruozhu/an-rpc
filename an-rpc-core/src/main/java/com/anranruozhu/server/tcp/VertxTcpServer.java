package com.anranruozhu.server.tcp;

import com.anranruozhu.server.HttpServer;
import com.anranruozhu.server.VertxHttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * @author anranruozhu
 * @className VertxTcpServer
 * @description 基于Vertx的tcp服务构建
 * @create 2024/7/27 下午4:37
 **/
public class VertxTcpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        //创建 Vert.x实例
        Vertx vertx=Vertx.vertx();

        //创建TCP服务器
        NetServer server=vertx.createNetServer();

        //处理请求
      server.connectHandler(new TCPServerHandle());
        //启动TCP服务器并监听指定端口
        server.listen(port,result ->{
          if (result.succeeded()){
              System.out.println("TCP server started on port "+port);
          }  else {
              System.out.println("Failed to start TCP server:"+result.cause());
          }
        });
    }

    public static void main(String[] args) {
        new VertxHttpServer().doStart(8888);
    }
}
