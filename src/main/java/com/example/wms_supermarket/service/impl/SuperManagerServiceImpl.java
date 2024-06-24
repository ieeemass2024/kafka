package com.example.wms_supermarket.service.impl;


import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.mapper.SuperManagerMapper;
import com.example.wms_supermarket.service.SuperManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperManagerServiceImpl implements SuperManagerService {
    @Autowired
    private SuperManagerMapper superManagerMapper;
    @Override
    public SuperManager queryById(int id) {
        return superManagerMapper.queryById(id);
    }

    @Override
    public List<User> queryBySuperId(int superId) {
        return superManagerMapper.queryBySuperId(superId);
    }
}
