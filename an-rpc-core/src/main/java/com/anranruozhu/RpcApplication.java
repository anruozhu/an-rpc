package com.anranruozhu;

import com.anranruozhu.config.RegistryConfig;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.constant.RpcConstant;
import com.anranruozhu.registry.Registry;
import com.anranruozhu.registry.RegistryFactory;
import com.anranruozhu.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;
/**
 * @author anranruozhu
 * @className RpcApplication
 * @description RPC框架应用，相当于holder，存放了项目全局用到的变量。双检锁单例模式实现
 * @create 2024/7/23 上午11:08
 **/
@Slf4j
public class RpcApplication {
    //单例模式
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig=newRpcConfig;
        log.info("RpcApplication init,config={}",newRpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig=rpcConfig.getRegistryConfig();
        Registry registry= RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init， config = {}",registryConfig);
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig= ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            //配置加载失败
            newRpcConfig=new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig==null){
            synchronized (RpcApplication.class){
                if(rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
