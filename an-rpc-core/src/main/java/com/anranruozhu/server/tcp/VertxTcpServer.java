package com.anranruozhu.server.tcp;


import com.anranruozhu.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anranruozhu
 * @className VertxTcpServer
 * @description 基于Vertx的tcp服务构建
 * @create 2024/7/27 下午4:37
 **/
@Slf4j
public class VertxTcpServer implements HttpServer{


    @Override
    public void doStart(int port) {
        //创建 Vert.x实例
        Vertx vertx=Vertx.vertx();

        //创建TCP服务器
        NetServer server=vertx.createNetServer();

        //处理请求
        server.connectHandler(new TCPServerHandle());
//        server.connectHandler(socket -> socket.handler(buffer -> {
//            //构造parser
//            //请求头是固定长度的
//            RecordParser parser=RecordParser.newFixed(8);
//            parser.setOutput(new Handler<Buffer>(){
//                //初始化
//                int size=-1;
//                //一次完整的读取（头+体）
//                Buffer resultBuffer=Buffer.buffer();
//                @Override
//                public void handle(Buffer buffer) {
//                    if (-1==size){
//                        //读取消息体的长度
//                        size=buffer.getInt(4);
//                        parser.fixedSizeMode(size);
//                        //写入头信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                    } else {
//                        //写入体信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                        System.out.println(resultBuffer.toString());
//                        //重置一轮
//                        parser.fixedSizeMode(8);
//                        size=-1;
//                        resultBuffer=Buffer.buffer();
//                    }
//                }
//            });
//
//        socket.handler(parser);
//        }));
        //启动TCP服务器并监听指定端口
        server.listen(port, res -> {
            if(res.succeeded()){
                log.info("Server started on port {}", port);
            } else {
                log.info("Server failed to start on port {}", port);
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
