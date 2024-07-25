package com.anranruozhu.config;

import lombok.Data;

/**
 * @author anranruozhu
 * @className RegistryConfig
 * @description RPC框架注册中心配置
 * @create 2024/7/25 下午3:34
 **/
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private  String registry="etcd";
    /**
     * 注册中心地址
     */
    private String address="http://1.94.191.237:2379";
    /**
     * 用户名
     */
    private String username;
    /**
     *  密码
     */
    private String password;
    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout=100000L;
}
