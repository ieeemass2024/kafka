package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.Message;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface GoodsService {

    public Message<Goods> queryById(HttpServletRequest httpServletRequest) throws Exception;
    public Message<List<Goods>> queryByName(HttpServletRequest httpServletRequest) throws Exception;
    public Message<List<Goods>> queryAll(HttpServletRequest httpServletRequest )throws Exception;
    public Message insert(HttpServletRequest httpServletRequest)throws Exception;
    public Message completeShelves(int gid,String location);
    public Message deleteGoods(int gid);
}
