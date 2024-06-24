package com.example.wms_supermarket.service;


import com.example.wms_supermarket.entity.Login;

public interface LoginService {
    public Login queryByNonce(int nonce);

    public int insert(Login login);
}