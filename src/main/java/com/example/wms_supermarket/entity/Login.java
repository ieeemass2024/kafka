package com.example.wms_supermarket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
//登录信息的实体类
public class Login {
    private int nonce;
    private int timestamp;
    private String time;
    public Login(){}
    public Login(int nonce, int timestamp){
        this.nonce=nonce;
        this.timestamp=timestamp;
        this.time= LocalDateTime.now().toString();
    }

}
