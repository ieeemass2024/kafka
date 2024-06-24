package com.example.wms_supermarket;

import com.alibaba.fastjson.JSONObject;
import com.example.wms_supermarket.service.impl.InstockTableServcieImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;



public class MyWebSocketClient extends WebSocketClient {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        MyWebSocketClient.applicationContext = applicationContext;
    }

    public MyWebSocketClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        // TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onOpen ------");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onClose ------"+arg1);
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onError ------"+arg0);
    }

    @Override
    public void onMessage(String arg0) {
        InstockTableServcieImpl instockTableServcie=applicationContext.getBean(InstockTableServcieImpl.class);
        // TODO Auto-generated method stub
        JSONObject jsonObject=JSONObject.parseObject(arg0);
        String code=jsonObject.getString("code");
        String msg=jsonObject.getString("msg");
        if (code.equals("0")&&msg.equals("释放成功")){
            instockTableServcie.completeInTable(Integer.parseInt(jsonObject.getString("data")));
        }
        else{
            System.out.println("------ MyWebSocket onMessage ------"+arg0);
        }
    }
}
