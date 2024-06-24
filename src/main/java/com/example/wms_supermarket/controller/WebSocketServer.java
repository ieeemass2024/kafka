package com.example.wms_supermarket.controller;


import com.alibaba.fastjson.JSONObject;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * WebSocket服务端
 * @author DavidLei
 *
 */
@Component
@ServerEndpoint("/truck-server/{clientId}")
public class WebSocketServer {
    private static int onlineCount = 0;
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<String, WebSocketServer>();
    private Session session;
    private String clientId;
    @OnOpen
    public void onOpen(@PathParam("clientId") String clientId, Session session) throws IOException {
        System.out.println("onOpen: has new client connect -"+clientId);
        this.clientId = clientId;
        this.session = session;
        addOnlineCount();
        clients.put(clientId, this);
        System.out.println("onOpen: now has "+onlineCount+" client online");
    }
    @OnClose
    public void onClose() throws IOException {
        System.out.println("onClose: has new client close connection -"+clientId);
        clients.remove(clientId);
        subOnlineCount();
        System.out.println("onClose: now has "+onlineCount+" client online");
    }
    @OnMessage
    public void onMessage(String message) throws Exception {
        System.out.println("onMessage: [clientId: " + clientId + " ,message:" + message + "]");
        JSONObject jsonObject=JSONObject.parseObject(message);
        int id = Integer.parseInt(jsonObject.getString("id"));
        System.out.println(id);
        JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
        if (id==1){
//            transportToStock(data);
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("onError: [clientId: " + clientId + " ,error:" + error.getCause() + "]");
    }
    public void sendMessageByClientId(String message, String clientId) throws IOException {
        for (WebSocketServer item : clients.values()) {
            if (item.clientId.equals(clientId) ) {
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }
    public void sendMessageAll(String message) throws IOException {
        for (WebSocketServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static synchronized Map<String,WebSocketServer> getClients() {
        return clients;
    }
//    public void transportToStock(JSONObject jsonObject) {
//        try {
//            TruckServiceImpl truckService=applicationContext.getBean(TruckServiceImpl.class);
//            System.out.println("1111");
//            Message<List<Truck>> msg = truckService.queryByStatus(0);
//            System.out.println("aaaa");
//            if (msg.getCode()==1){
//                System.out.println("kkkk");
//                sendMessageByClientId(msg.toJson(),clientId);
//                System.out.println("cccc");
//                return;
//            }
//            System.out.println("2222");
//            List<Truck> trucks=msg.getData();
//            Random random = new Random();
//            int randomIndex = random.nextInt(trucks.size());
//            Truck truck=trucks.get(randomIndex);
//            System.out.println("3333");
//            String gid=jsonObject.getString("gid");
//            String tableId=jsonObject.getString("tid");
//            System.out.println("4444");
//            Message msg1=truckService.occupyTruck(truck.getTid(),Integer.parseInt(gid),Integer.parseInt(tableId));
//            System.out.println("5555");
//            sendMessageByClientId(msg1.toJson(),clientId);
//            int n=Util.generateRandomDelay() ;
//            for(int i=0;i<n;i++){
//                sendMessageByClientId("小车编号"+truck.getTid()+"，正在为您搬运货物，预计在"+(n-i)+"秒内完成。",clientId);
//                Thread.sleep(1000);
//
//            }
//            Message msg2=truckService.freeTruck(truck.getTid(),Integer.parseInt(tableId));
//            sendMessageByClientId(msg2.toJson(),clientId);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
