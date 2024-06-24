package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.entity.User;

import java.util.List;

public interface SuperManagerService {
    public SuperManager queryById(int id);
    public List<User> queryBySuperId(int superId);
}
