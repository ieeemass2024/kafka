package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.Record;
import java.util.List;

public interface RecordService {
    public List<Record> queryByTid(int tid);
    public void insertRecord(com.example.wms_supermarket.entity.Record record);

    public List<Record> queryAll();
}
