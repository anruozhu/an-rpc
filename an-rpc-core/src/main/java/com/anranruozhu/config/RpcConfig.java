package com.anranruozhu.config;

import com.anranruozhu.serializer.Serializer;
import com.anranruozhu.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author anranruozhu
 * @className RpcConfig
 * @description RPC框架配置
 * @create 2024/7/23 上午10:55
 **/
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name="an-rpc";
    /**
     * 版本号
     */
    private String version="1.0";
    /**
     * 服务主机地址
     */
    private String serverHost="localhost";
    /**
     * 服务端口
     */
    private int serverPort=8080;

    /**
     * 模拟调用
     */
    private boolean mock=false;

    private String serializer= SerializerKeys.JDK;
}
