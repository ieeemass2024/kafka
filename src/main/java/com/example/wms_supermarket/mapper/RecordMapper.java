package com.example.wms_supermarket.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.wms_supermarket.entity.Record;
import java.util.List;
@Mapper
public interface RecordMapper {
    public void insertRecord(com.example.wms_supermarket.entity.Record record);
    public List<Record> queryByTid(int tid);
    public List<Record> queryAll();
}
