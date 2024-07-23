package com.anranruozhu.consumer;

import com.anranruozhu.common.model.User;
import com.anranruozhu.common.service.UserService;

/**
 * @author anranruozhu
 * @className EasyConsumerExample
 * @description 简易服务消费者示例
 * @create 2024/7/21 下午4:23
 **/
public class EasyConsumerExample {
    public static void main(String[] args) {
        UserService userService=new UserServiceProxy();
        User user=new User();
        user.setName("anranruozhu");
        //调用服务进行消费
        User newUser=userService.getUser(user);
        if(newUser!=null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user is null");
        }
    }
}
