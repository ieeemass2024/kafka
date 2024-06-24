package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.OutStockTable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OutStockTableService {
    public Message<List<OutStockTable>> queryAll(HttpServletRequest httpServletRequest);
    public Message<OutStockTable> queryByTid(HttpServletRequest httpServletRequest);
    public Message<List<OutStockTable>> queryByGid(HttpServletRequest httpServletRequest);
    public Message<List<OutStockTable>> queryByStatus(HttpServletRequest httpServletRequest);
    public Message insertOutTable(HttpServletRequest httpServletRequest);
    public Message grantOutTable(HttpServletRequest httpServletRequest);
    public Message completeOutTable(int tid);
}
