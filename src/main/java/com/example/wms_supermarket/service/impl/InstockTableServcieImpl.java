package com.example.wms_supermarket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.wms_supermarket.MyWebSocketClient;
import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.*;
import com.example.wms_supermarket.mapper.GoodsMapper;
import com.example.wms_supermarket.mapper.InStockTableMapper;
import com.example.wms_supermarket.mapper.OutStockTableMapper;
import com.example.wms_supermarket.mapper.UserMapper;
import com.example.wms_supermarket.service.InStockTableService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InstockTableServcieImpl implements InStockTableService {
    @Autowired
    private InStockTableMapper inStockTableMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    private Message oauth(HttpServletRequest httpServletRequest){
        Message<List<Goods>> msg=new Message<>();
        Cookie[] cookies=httpServletRequest.getCookies();
        String id= Util.getCookieValue(cookies,"userId");
        if (id==null){
            msg.setCode(102);
            msg.setMsg("当前身份无权限查看");
            return msg;
        }
        User user =userMapper.queryById(Integer.parseInt(id));
        if(user==null){
            msg.setCode(103);
            msg.setMsg("登录信息有误");
        }
        else if (!user.getRole().equals("2") && !user.getRole().equals("1")){
            msg.setCode(102);
            msg.setMsg("当前身份无权限查看");
        }
        else{
            msg.setCode(0);
            msg.setMsg("可以进行查询");
        }
        return msg;
    }
    @Override
    public Message<List<InStockTable>> queryAll(HttpServletRequest httpServletRequest) {
        Message<List<InStockTable>> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            msg.setCode(0);
            msg.setMsg("查询成功");
            msg.setData(inStockTableMapper.queryAll());
        }
        return msg;
    }

    @Override
    public Message<InStockTable> queryByTid(HttpServletRequest httpServletRequest) {
        Message <InStockTable> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String tid=httpServletRequest.getParameter("tid");
            if (tid==null){
                msg.setCode(105);
                msg.setMsg("传入参数错误");
                return msg;
            }
            msg.setCode(0);
            msg.setMsg("查询成功");
            msg.setData(inStockTableMapper.queryByTid(Integer.parseInt(tid)));
        }
        System.out.println("发送的信息");
        System.out.println(msg.toJson());
        return msg;
    }

    @Override
    public Message<List<InStockTable>> queryByGid(HttpServletRequest httpServletRequest) {
        Message <List<InStockTable>> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String gid=httpServletRequest.getParameter("gid");
            if (gid==null){
                msg.setCode(105);
                msg.setMsg("传入参数错误");
                return msg;
            }
            msg.setCode(0);
            msg.setMsg("查询成功");
            msg.setData(inStockTableMapper.queryByGid(Integer.parseInt(gid)));
        }
        return msg;
    }

    @Override
    public Message<List<InStockTable>> queryByStatus(HttpServletRequest httpServletRequest) {
        Message <List<InStockTable>> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String status=httpServletRequest.getParameter("status");
            if (status==null){
                msg.setCode(105);
                msg.setMsg("传入参数错误");
                return msg;
            }
            msg.setCode(0);
            msg.setMsg("查询成功");
            msg.setData(inStockTableMapper.queryByStatus(Integer.parseInt(status)));
        }
        return msg;
    }

    @Override
    public Message insertInTable(int gid) {
Message msg=new Message();
            int tid=Util.generateId();
            msg.setCode(0);
            msg.setMsg("插入成功");
            inStockTableMapper.insertInTable(tid,gid);

        return msg;
    }

    @Override
    public Message grantInTable(HttpServletRequest httpServletRequest) {
        Message msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String tid=httpServletRequest.getParameter("tid");
            if (tid==null){
                msg.setCode(105);
                msg.setMsg("传入参数错误");
                return msg;
            }
            msg.setCode(0);
            msg.setMsg("插入成功");
            inStockTableMapper.grantInTable(Integer.parseInt(tid));

        }
        return msg;
    }
    @Override
    public Message completeInTable(int tid) {
        Message msg=new Message();
        msg.setCode(0);
        msg.setMsg("货物入库");
        inStockTableMapper.completeInTable(tid);
        return msg;
    }

//    @Override
//    public Message sendMessageToTruck(HttpServletRequest httpServletRequest, WebSocketClient webSocketClient) {
//        Message msg=new Message<>();
//        Message oauthMsg=oauth(httpServletRequest);
//        if (oauthMsg.getCode()!=0){
//            msg.setCode(oauthMsg.getCode());
//            msg.setMsg(oauthMsg.getMsg());
//        }
//        else{
//            String tid=httpServletRequest.getParameter("tid");
//            if (tid==null){
//                msg.setCode(105);
//                msg.setMsg("传入参数错误");
//                return msg;
//            }
//
//            InStockTable inStockTable= inStockTableMapper.queryByTid(Integer.parseInt(tid));
//            if (inStockTable.getStatus()!=1){
//                msg.setCode(1);
//                msg.setMsg("该订单未被批准");
//            }
//            else{
//                msg.setCode(0);
//                msg.setMsg("插入成功");
//                JSONObject jsonObject=JSONObject.parseObject(inStockTable.toJson());
//                WebSocketMessage webSocketMessage=new WebSocketMessage();
//                webSocketMessage.setId(1);
//                webSocketMessage.setData(jsonObject);
//                webSocketClient.send(webSocketMessage.toJson());
//            }
//
//        }
//        return msg;
//    }
}
