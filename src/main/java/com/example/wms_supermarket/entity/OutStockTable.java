package com.example.wms_supermarket.entity;

import com.google.gson.Gson;
import lombok.Data;

@Data
//出库单实体类
public class OutStockTable {
    private int tid;
    private int status;//待审核2，待入库1，已入库0
    private int gid;
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
