package com.anranruozhu.common.model;

import java.io.Serializable;

/**
 * @author anranruozhu
 * @className User
 * @description 用户类
 * @create 2024/7/21 下午4:08
 **/
public class User implements Serializable {

    private static final long serialVersionUID = 8467605349350780004L;

    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
