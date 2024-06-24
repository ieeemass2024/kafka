package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.entity.InStockTable;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.mapper.InStockTableMapper;
import com.example.wms_supermarket.service.KafkaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaServiceImpl implements KafkaService {
    @Autowired
    private KafkaTemplate<Object, Object> template;
    @Autowired
    private InStockTableMapper inStockTableMapper;
    @Override
    public Message intoStock(HttpServletRequest httpServletRequest) {
        Message msg=new Message<>();
        String tid=httpServletRequest.getParameter("tid");
        if (tid==null){
            msg.setCode(105);
            msg.setMsg("传入参数错误");
            return msg;
        }
        InStockTable inStockTable=inStockTableMapper.queryByTid(Integer.parseInt(tid));
        Message msgToSend = new Message();
        msgToSend.setCode(0);
        msgToSend.setMsg("货物准备入库");
        msgToSend.setData(inStockTable);
        msg.setCode(0);
        msg.setMsg("插入成功");
        template.send("wms_topic_into_stock",msgToSend.toJson());
        return msg;
    }
}
