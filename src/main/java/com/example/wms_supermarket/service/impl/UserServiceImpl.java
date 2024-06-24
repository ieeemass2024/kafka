package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.mapper.UserMapper;
import com.example.wms_supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserMapper userMapper;
    @Override
    public User queryById(int id) {
        return userMapper.queryById(id);
    }

    @Override
    public void registerUser(User user) {
        userMapper.registerUser(user);
    }

    @Override
    public void addUser(int id) {
        userMapper.addUser(id);
    }

    @Override
    public List<User> queryAll(int id) {
        return userMapper.queryAll(id);
    }

    @Override
    public List<User> queryByRegister(int id) {
        return userMapper.queryByRegister(id);
    }

    @Override
    public void delete(int id) {
        userMapper.delete(id);
    }


}
