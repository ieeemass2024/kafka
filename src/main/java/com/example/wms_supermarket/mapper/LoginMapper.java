package com.example.wms_supermarket.mapper;


import com.example.wms_supermarket.entity.Login;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    public Login queryByNonce(int nonce);
    public int insert(Login login);
}
