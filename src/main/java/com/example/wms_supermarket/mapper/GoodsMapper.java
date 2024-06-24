package com.example.wms_supermarket.mapper;

import com.example.wms_supermarket.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    public Goods queryById(int gid);
    public List<Goods> queryByName(String name);
    public List<Goods> queryAll();
    public int insertGoods(int gid,String name,String description,String location);
    public int deleteGoods(int gid);
    public int updateGoodsLocation(int gid,String location);
}
