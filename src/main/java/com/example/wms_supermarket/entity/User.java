package com.example.wms_supermarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.Gson;
import lombok.Data;

@Data
//用户的实体类
public class User {
    @TableId(value = "userId",type = IdType.AUTO)
    private int userId;
    private String name;
    private String password;
    private String realName;
    private int superId;
    private String role;
    private int isRegister;
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
