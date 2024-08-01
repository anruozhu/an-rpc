package com.anranruozhu.fault.retry;

import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;

import java.util.concurrent.Callable;

/**
 * @author anranruozhu
 * @className RetryStrategy
 * @description 重试策略
 * @create 2024/8/1 下午2:57
 **/
public interface RetryStrategy {


    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RPCResponse doRetry(Callable<RPCResponse> callable) throws Exception;
}
