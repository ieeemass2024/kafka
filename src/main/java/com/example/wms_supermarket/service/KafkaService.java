package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.Message;
import jakarta.servlet.http.HttpServletRequest;

public interface KafkaService {
    public Message intoStock(HttpServletRequest httpServletRequest);
}
