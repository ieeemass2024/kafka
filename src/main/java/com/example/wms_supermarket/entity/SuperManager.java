package com.example.wms_supermarket.entity;

import lombok.Data;

@Data
//超级管理员的实体类
public class SuperManager {
    private int superId;
    private String name;
    private String password;
    private String realName;
}
