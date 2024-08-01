package com.anranruozhu.fault.retry;

import com.anranruozhu.spi.SpiLoader;

/**
 * @author anranruozhu
 * @className RetryStrategyFactory
 * @description 工厂模式加载重试策略
 * @create 2024/8/1 下午3:20
 **/
public class RetryStrategyFactory {
    static{
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
