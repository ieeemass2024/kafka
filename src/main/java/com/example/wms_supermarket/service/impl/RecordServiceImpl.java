package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.mapper.RecordMapper;
import com.example.wms_supermarket.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.wms_supermarket.entity.Record;
import java.util.List;
@Service
public class RecordServiceImpl implements RecordService {
@Autowired
private RecordMapper recordMapper;
    @Override
    public List<Record> queryByTid(int tid) {
        return recordMapper.queryByTid(tid);
    }

    @Override
    public void insertRecord(com.example.wms_supermarket.entity.Record record) {
        recordMapper.insertRecord(record);
    }



    @Override
    public List<Record> queryAll() {
        return recordMapper.queryAll();
    }
}
