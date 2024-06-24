package com.example.wms_supermarket.entity;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.Data;

@Data
//websocket传递的信息的实体类
public class WebSocketMessage {
    private int id;
    private JSONObject data;
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
