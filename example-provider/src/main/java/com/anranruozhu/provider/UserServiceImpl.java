package com.anranruozhu.provider;

import com.anranruozhu.common.model.User;
import com.anranruozhu.common.service.UserService;

/**
 * @author anranruozhu
 * @className UserServiceImpl
 * @description 用户服务实现类
 * @create 2024/7/21 下午4:19
 **/
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("UserName:"+user.getName());
        return user;
    }
}
