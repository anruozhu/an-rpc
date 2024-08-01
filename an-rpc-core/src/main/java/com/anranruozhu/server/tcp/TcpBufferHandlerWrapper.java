package com.anranruozhu.server.tcp;

import com.anranruozhu.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;



/**
 * @author anranruozhu
 * @className TcpBufferHandlerWrapper
 * @description 装饰者模式（使用 recordParser 对原有得buffer 处理能力进行增强）
 * @create 2024/7/29 下午3:18
 **/
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;
    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser=initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler){

        //构造parser
        RecordParser parser=RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);
       parser.setOutput(new Handler<Buffer>() {
               //初始化
               int size=-1;
               //一次完整的读取（头+体）
               Buffer resultBuffer=Buffer.buffer();

               @Override
               public void handle(Buffer buffer) {
                   if (-1==size){
                       //读取消息体的长度
                       size=buffer.getInt(13);
                       parser.fixedSizeMode(size);
                       //写入头信息到结果
                       resultBuffer.appendBuffer(buffer);
                   } else {
                       // 2. 然后读取消息体
                       //写入体信息到结果
                       resultBuffer.appendBuffer(buffer);
                       // 已拼接为完整 Buffer，执行处理
                       bufferHandler.handle(resultBuffer);
                       //重置一轮
                       parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                       size=-1;
                       resultBuffer=Buffer.buffer();
                   }
               }
       });
        return parser;
    }
}
