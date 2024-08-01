package com.anranruozhu.fault.retry;

import com.anranruozhu.model.RPCResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author anranruozhu
 * @className FixedIntervalRetryStrategy
 * @description 固定时间间隔--重试策略
 * @create 2024/8/1 下午3:04
 **/
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    @Override
    public RPCResponse doRetry(Callable<RPCResponse> callable) throws ExecutionException, RetryException {
        Retryer<RPCResponse> retryer= RetryerBuilder.<RPCResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                //重试等待时间
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                //重试最大次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener(){
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数{}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
