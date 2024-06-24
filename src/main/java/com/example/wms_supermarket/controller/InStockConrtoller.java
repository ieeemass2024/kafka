package com.example.wms_supermarket.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wms_supermarket.MyWebSocketClient;
import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.InStockTable;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.Record;
import com.example.wms_supermarket.service.impl.GoodServiceImpl;
import com.example.wms_supermarket.service.impl.InstockTableServcieImpl;
import com.example.wms_supermarket.service.impl.RecordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
@Tag(name = "InStockController", description = "管理入库的接口")
@RestController
public class InStockConrtoller {
    @Autowired
    private GoodServiceImpl goodService;
    @Autowired
    private InstockTableServcieImpl inStockTableService;
    @Autowired
    private KafkaTemplate<Object, Object> template;
    @Autowired
    private RecordServiceImpl recordService;
    @Autowired
    private WebSocketServer webSocketServer;
    @Operation(summary = "获取所有的入库单信息", description = "获取所有的入库单信息")
    @Parameters({
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "所有的入库单信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getAllTable")
    public void getAllTable(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<List<InStockTable>> msg = inStockTableService.queryAll(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "获取某种状态的入库单信息", description = "获取某种状态的入库单信息")
    @Parameters({
            @Parameter(name = "status", required = true,description="入库单的状态，2为待审核，1为入库中，0为已入库"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "某种状态的入库单信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getTableByStatus")
    public void getTableByStatus(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<List<InStockTable>> msg = inStockTableService.queryByStatus(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "获取某个id的入库单信息", description = "获取某个id的入库单信息")
    @Parameters({
            @Parameter(name = "tid", required = true,description="入库单的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "某个id的入库单信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getTableByTid")
    public void getTableByTid(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<InStockTable> msg = inStockTableService.queryByTid(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "获取货物id为gid的入库单信息", description = "获取货物id为gid的入库单信息")
    @Parameters({
            @Parameter(name = "gid", required = true,description="入库单的货物的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "货物id为gid的入库单信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getTableByGid")
    public void getTableByGid(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<List<InStockTable>> msg = inStockTableService.queryByGid(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "生成新的入货单", description = "生成新的入货单")
    @Parameters({
            @Parameter(name = "name", required = true,description="货物名称"),
            @Parameter(name = "description", required = true,description="货物的描述"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "生成入库单的结果信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/insertTable")
    public void insertTable(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<Goods> msg1=goodService.insert(httpServletRequest);
        if (msg1.getCode()==0){
            Message msg = inStockTableService.insertInTable(msg1.getData().getGid());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().println(msg.toJson());
        }
        else{
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().println(msg1.toJson());
        }
    }

    @Operation(summary = "审核通过某个id的入库单", description = "审核通过某个id的入库单")
    @Parameters({
            @Parameter(name = "tid", required = true,description="需要审核的入库单的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "审核结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/grantTable")
    public void grantTable(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message msg = inStockTableService.grantInTable(httpServletRequest);
        System.out.println(msg);
        Message<InStockTable> msg1=inStockTableService.queryByTid(httpServletRequest);
        if (msg1.getCode() == 0) {
            System.out.println("发送的信息");
            System.out.println(msg1.toJson());
            template.send("wms_to_shelves_topic_into_stock",msg1.toJson());
        }
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    //监听货车情况的监听器
    @KafkaListener(id = "wms_listen_to_truck", topics = "truck_to_wms_topic_into_stock")
    public void listen_truck(String input) {
        JSONObject jsonObject=JSONObject.parseObject(input);
        String code=jsonObject.getString("code");
        String msg=jsonObject.getString("msg");
        System.out.println(msg);
        if (!code.equals("106")){
            JSONObject data=JSONObject.parseObject(jsonObject.getString("data"));
            String tid=data.getString("tid");
            //记录货车运货的情况，方便为用户展示
            recordService.insertRecord(new Record(Integer.parseInt(tid),msg, LocalDateTime.now().toString(),"入库"));
            if (code.equals("1")){
                System.out.println("还在入库中");
            }
            else{
                System.out.println("已经入库了");
            }
        }
    }
    //监听货架情况的监听器
    @KafkaListener(id = "wms_listen_to_shelves", topics = "shelves_to_wms_topic_into_stock")
    public void listen_shelves(String input) {
        JSONObject jsonObject=JSONObject.parseObject(input);
        String code=jsonObject.getString("code");
        String msg=jsonObject.getString("msg");
        System.out.println(msg);
        JSONObject data=JSONObject.parseObject(jsonObject.getString("data"));
        String tid=data.getString("tid");
        String gid=data.getString("gid");
        String shId=data.getString("shId");
        if (code.equals("106")){
            //货架已满，不做通知，入库失败
            System.out.println("库存已满");
        }
        else{
            //入库成功，通过websocket告知前端
            System.out.println("已经入库了");
            try {
                webSocketServer.sendMessageAll(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inStockTableService.completeInTable(Integer.parseInt(tid));
            goodService.completeShelves(Integer.parseInt(gid),shId);
        }
    }
}
