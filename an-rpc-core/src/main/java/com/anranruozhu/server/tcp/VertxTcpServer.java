package com.anranruozhu.server.tcp;


import com.anranruozhu.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.grpc.VertxServer;
//import lombok.extern.slf4j.Slf4j;

/**
 * @author anranruozhu
 * @className VertxTcpServer
 * @description 基于Vertx的tcp服务构建
 * @create 2024/7/27 下午4:37
 **/
//@Slf4j
public class VertxTcpServer {


    public void doStart(int port) {
        //创建 Vert.x实例
        Vertx vertx=Vertx.vertx();

        //创建TCP服务器
        NetServer server=vertx.createNetServer();

        //处理请求
        // server.connectHandler(new TCPServerHandle());
//        server.connectHandler(socket -> socket.handler(buffer -> {
//            String testMessage = "Hello, server!Hello, server!";
//            System.out.println(buffer.toString());
//            int messageLength = testMessage.getBytes().length;
//            // 构造parser
//            RecordParser parser = RecordParser.newFixed(messageLength);
//            parser.setOutput(new Handler<Buffer>() {
//                @Override
//                public void handle(Buffer buffer) {
//                    String str = new String(buffer.getBytes());
//                    System.out.println(str);
//                    if (testMessage.equals(str)) {
//                        System.out.println("good");
//                    } else {
//                        System.out.println("bad: " + str);
//                    }
//                }
//            });
//            parser.handle(buffer);
//            socket.handler(parser);
//        }));
        //启动TCP服务器并监听指定端口
        server.connectHandler(socket -> {
            System.out.println(socket);
            socket.handler(buffer -> {
                System.out.println(buffer.toString());
            });
            socket.closeHandler(System.out::println);
        }).exceptionHandler(Throwable::printStackTrace).listen(port,"127.0.0.1", result ->{
          if (result.succeeded()){
              System.out.println("TCP server started on port "+port);
          }  else {
              System.out.println("Failed to start TCP server:"+result.cause());
          }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
