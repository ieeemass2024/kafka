package com.example.wms_supermarket.mapper;

import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SuperManagerMapper {
    public SuperManager queryById(int id);
    public List<User> queryBySuperId(int superId);
}
