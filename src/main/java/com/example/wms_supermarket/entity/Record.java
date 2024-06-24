package com.example.wms_supermarket.entity;

import com.google.gson.Gson;
import lombok.Data;

import java.time.LocalDateTime;

@Data
//出入库记录的实体类
public class Record {
    int tid;
    String msg;
    String time;
    String type;
    public Record(){}
    public Record(int tid,String msg,String time,String type){
        this.msg=msg;
        this.tid=tid;
        this.time=time;
        this.type=type;
    }
    public Record(int tid,String msg,LocalDateTime time){
        this.msg=msg;
        this.tid=tid;
        this.time=time.toString();
    }
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
