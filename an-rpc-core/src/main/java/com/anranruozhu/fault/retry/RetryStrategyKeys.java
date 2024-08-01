package com.anranruozhu.fault.retry;

/**
 * @author anranruozhu
 * @className RetryStrategyKeys
 * @description 重试策略键名常量
 * @create 2024/8/1 下午3:17
 **/
public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO="no";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL="fixedInterval";
}
