package com.example.wms_supermarket.service.impl;


import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.mapper.GoodsMapper;
import com.example.wms_supermarket.mapper.UserMapper;
import com.example.wms_supermarket.service.GoodsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodServiceImpl implements GoodsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodMapper;
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
    public Message<Goods> queryById(HttpServletRequest httpServletRequest) {
        Message<Goods> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String gid=httpServletRequest.getParameter("gid");
            if (gid==null){
                msg.setCode(105);
                msg.setMsg("输入参数错误");
            }
            else{
                msg.setCode(0);
                msg.setMsg("查询成功");
                msg.setData(goodMapper.queryById(Integer.parseInt(gid)));
            }
        }
        return msg;
    }

    @Override
    public Message<List<Goods>> queryByName(HttpServletRequest httpServletRequest) {
        Message<List<Goods>> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String name=httpServletRequest.getParameter("name");
            if (name==null){
                msg.setCode(105);
                msg.setMsg("输入参数错误");
            }
            else{
                msg.setCode(0);
                msg.setMsg("查询成功");
                msg.setData(goodMapper.queryByName(name));
            }
        }
        return msg;
    }

    @Override
    public Message<List<Goods>> queryAll(HttpServletRequest httpServletRequest) throws Exception {
        Message<List<Goods>> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            msg.setCode(0);
            msg.setMsg("查询成功");
            msg.setData(goodMapper.queryAll());
        }
        return msg;
    }

    @Override
    public Message<Goods> insert(HttpServletRequest httpServletRequest) throws Exception {
        Message<Goods> msg=new Message<>();
        Message oauthMsg=oauth(httpServletRequest);
        if (oauthMsg.getCode()!=0){
            msg.setCode(oauthMsg.getCode());
            msg.setMsg(oauthMsg.getMsg());
        }
        else{
            String name=httpServletRequest.getParameter("name");
            String description = httpServletRequest.getParameter("description");
            String location="还未入库";
            if (name==null||description==null){
                msg.setCode(105);
                msg.setMsg("输入参数错误");
            }
            else{
                msg.setCode(0);
                msg.setMsg("插入成功");
                int gid=Util.generateId();
                Goods goods=new Goods(gid,name,description,location);
                goodMapper.insertGoods(gid,name,description,location);
                msg.setData(goods);
            }
        }
        return msg;
    }

    @Override
    public Message completeShelves(int gid, String location) {
        Message msg=new Message();
        msg.setMsg("成功上架");
        msg.setCode(0);
        goodMapper.updateGoodsLocation(gid,location);
        return msg;
    }

    @Override
    public Message deleteGoods(int gid) {
        Message msg=new Message();
        msg.setMsg("成功出库");
        msg.setCode(0);
        System.out.println("出库了");
        System.out.println(gid);
        goodMapper.deleteGoods(gid);
        return msg;
    }

}
