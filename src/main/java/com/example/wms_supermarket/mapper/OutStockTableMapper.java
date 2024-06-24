package com.example.wms_supermarket.mapper;

import com.example.wms_supermarket.entity.OutStockTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutStockTableMapper {
    public List<OutStockTable> queryAll();
    public OutStockTable queryByTid(int tid);
    public List<OutStockTable> queryByGid(int gid);
    public List<OutStockTable> queryByStatus(int status);
    public int insertOutTable(int tid,int gid);
    public int grantOutTable(int tid);
    public int completeOutTable(int tid);
}
