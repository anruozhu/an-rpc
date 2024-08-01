package com.anranruozhu.fault.retry;


import com.anranruozhu.model.RPCResponse;
import org.junit.Test;

public class RetryStrategyTest  {
    RetryStrategy retryStrategy=new FixedIntervalRetryStrategy();
    @Test
    public void doRetry(){
        try {
            RPCResponse rpcResponse=retryStrategy.doRetry(()->{
                System.out.println("测试重试");
                throw new RuntimeException("模拟测试失败！");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败！");
            e.printStackTrace();
        }
    }
}