package com.anranruozhu.fault.retry;

import com.anranruozhu.model.RPCRequest;
import com.anranruozhu.model.RPCResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author anranruozhu
 * @className NoRetryStrategy
 * @description 不重试--重试策略
 * @create 2024/8/1 下午3:01
 **/
@Slf4j
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RPCResponse doRetry(Callable<RPCResponse> callable) throws Exception {
        return callable.call();
    }
}
