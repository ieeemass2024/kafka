package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.service.impl.GoodServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/goods")
@RestController
@Tag(name = "GoodsController", description = "管理货物的接口")
public class GoodsController {
    @Autowired
    private GoodServiceImpl goodService;

    @Operation(summary = "通过货物id获取单个货物信息", description = "获取货物")
    @Parameters({
            @Parameter(name = "gid", required = true,description="货物的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "特定id的货物", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getGoodsById")
    public void getGoodsById(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<Goods> msg = goodService.queryById(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "通过货物名称获取单个货物信息", description = "获取货物")
    @Parameters({
            @Parameter(name = "name", required = true,description="货物的名称"),
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "特定名称的货物", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getGoodsByName")
    public void getGoodsByName(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<List<Goods>> msg = goodService.queryByName(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
    @Operation(summary = "获取所有的货物", description = "获取货物")
    @Parameters({
            @Parameter(in = ParameterIn.COOKIE,name = "userId",required = true,description="用户的id"),
            @Parameter(in = ParameterIn.COOKIE,name = "token",required = true,description="用户的登录令牌"),
    })
    @ApiResponse(description = "所有入库的货物", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @GetMapping(path = "/getAll")
    public void getAll(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        Message<List<Goods>> msg = goodService.queryAll(httpServletRequest);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(msg.toJson());
    }
}
