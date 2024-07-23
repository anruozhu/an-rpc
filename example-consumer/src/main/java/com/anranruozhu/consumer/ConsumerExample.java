package com.anranruozhu.consumer;

import com.anranruozhu.common.model.User;
import com.anranruozhu.common.service.UserService;
import com.anranruozhu.config.RpcConfig;
import com.anranruozhu.proxy.ServiceProxyFactory;
import com.anranruozhu.utils.ConfigUtils;

/**
 * @author anranruozhu
 * @className ConsumerExample
 * @description 服务消费者示例
 * @create 2024/7/23 上午11:21
 **/
public class ConsumerExample {
    public static void main(String[] args) {

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user=new User();
        user.setName("anranruozhu");
        //调用服务进行消费
        User newUser=userService.getUser(user);
        if(newUser!=null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user is null");
        }
        long number=userService.getNumber();
        System.out.println(number);
    }
}
