package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.User;

import java.util.List;

public interface UserService {
    public User queryById(int id);
    public void registerUser(User user);
    public void addUser(int id);
    public List<User> queryAll(int id);
    public List<User> queryByRegister(int id);
    public void delete(int id);
}
