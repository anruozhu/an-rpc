package com.anranruozhu.server.tcp;

import io.vertx.core.Vertx;

/**
 * @author anranruozhu
 * @className VerxTcpClient
 * @description 基于Vertx的tcp客户端
 * @create 2024/7/27 下午4:47
 **/


public class VertxTcpClient {

    public void start() {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();
                socket.handler(buffer -> System.out.println(buffer.toString()));
                for (int i = 0; i < 10; i++) {
                    // 发送数据
                    socket.write("Hello, server!Hello, server!");
                }

            } else {
                System.err.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}

