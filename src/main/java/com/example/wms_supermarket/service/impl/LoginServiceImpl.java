package com.example.wms_supermarket.service.impl;


import com.example.wms_supermarket.entity.Login;
import com.example.wms_supermarket.mapper.LoginMapper;
import com.example.wms_supermarket.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public Login queryByNonce(int nonce) {
        return loginMapper.queryByNonce(nonce);
    }

    @Override
    public int insert(Login login) {
        return loginMapper.insert(login);
    }
}
