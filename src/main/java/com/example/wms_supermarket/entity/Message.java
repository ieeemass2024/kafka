package com.example.wms_supermarket.entity;

import com.google.gson.Gson;
import lombok.Data;

@Data
//传递信息的实体类
public class Message<T> {
    private int code;//状态码
    private String msg;//信息
    private T data=null;//传递的实例化对象
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
