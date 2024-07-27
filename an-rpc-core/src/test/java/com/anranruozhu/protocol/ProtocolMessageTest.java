package com.anranruozhu.protocol;


import cn.hutool.core.util.IdUtil;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.model.RPCRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProtocolMessageTest  {
    @Test
    public void testEncodeAndDecode() throws IOException {
        ProtocolMessage<RPCRequest>protocolMessage =new ProtocolMessage<>();
        ProtocolMessage.Header header =new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte)ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte)ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        RPCRequest rpcRequest =new RPCRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setParams(new Object[]{"aaa","bbb"});
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);
        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?>message =ProtocolMessageDecoder.decode(encodeBuffer);
        Assert.assertNotNull(message);
    }

}