package com.example.wms_supermarket.service;

import com.example.wms_supermarket.MyWebSocketClient;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.InStockTable;
import jakarta.servlet.http.HttpServletRequest;
import org.java_websocket.client.WebSocketClient;

import java.util.List;

public interface InStockTableService {
    public Message<List<InStockTable>> queryAll(HttpServletRequest httpServletRequest);
    public Message<InStockTable> queryByTid(HttpServletRequest httpServletRequest);
    public Message<List<InStockTable>> queryByGid(HttpServletRequest httpServletRequest);
    public Message<List<InStockTable>> queryByStatus(HttpServletRequest httpServletRequest);
    public Message insertInTable(int gid);
    public Message grantInTable(HttpServletRequest httpServletRequest);
//    public Message sendMessageToTruck(HttpServletRequest httpServletRequest, WebSocketClient webSocketClient);
    public Message completeInTable(int tid);
}
