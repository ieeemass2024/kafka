package com.example.wms_supermarket.mapper;

import com.example.wms_supermarket.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public User queryById(int id);
    public void registerUser(User user);
    public void addUser(int id);
    public List<User> queryAll(int id);
    public List<User> queryByRegister(int id);
    public void delete(int id);
}
