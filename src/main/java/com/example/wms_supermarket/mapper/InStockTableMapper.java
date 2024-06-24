package com.example.wms_supermarket.mapper;

import com.example.wms_supermarket.entity.InStockTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InStockTableMapper {
    public List<InStockTable> queryAll();
    public InStockTable queryByTid(int tid);
    public List<InStockTable> queryByGid(int gid);
    public List<InStockTable> queryByStatus(int status);
    public int insertInTable(int tid,int gid);
    public int grantInTable(int tid);
    public int completeInTable(int tid);
}
