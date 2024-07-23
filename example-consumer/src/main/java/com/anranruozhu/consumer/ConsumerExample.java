package com.anranruozhu.consumer;

import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.utils.ConfigUtils;

/**
 * @author anranruozhu
 * @className ConsumerExample
 * @description 服务消费者示例
 * @create 2024/7/23 上午11:21
 **/
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc= ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpc);
    }
}
