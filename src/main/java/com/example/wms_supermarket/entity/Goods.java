package com.example.wms_supermarket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
//货物的实体类
public class Goods {
    private int gid;
    private String name;
    private String description;
    private String location;
    public Goods(){};
    public Goods(int goodId,String name,String description,String location){
        this.gid=goodId;
        this.name=name;
        this.description=description;
        this.location=location;
    }

}
