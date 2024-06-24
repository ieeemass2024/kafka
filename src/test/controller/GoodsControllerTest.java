package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.service.impl.GoodServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GoodsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoodServiceImpl goodService;

    @InjectMocks
    private GoodsController goodsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(goodsController).build();
    }

    @Test
    void testGetGoodsById() throws Exception {
        Goods mockGoods = new Goods(1, "Apple", "A sweet fruit", "Fruit section");
        Message<Goods> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(mockGoods);

        when(goodService.queryById(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/goods/getGoodsById")
                        .param("id", "1")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":{\"gid\":1,\"name\":\"Apple\",\"description\":\"A sweet fruit\",\"location\":\"Fruit section\"}}"));
    }

    @Test
    void testGetGoodsByName() throws Exception {
        Goods mockGoods1 = new Goods(1, "Apple", "A sweet fruit", "Fruit section");
        Goods mockGoods2 = new Goods(2, "Apple Juice", "A sweet drink", "Beverage section");
        Message<List<Goods>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(List.of(mockGoods1, mockGoods2));

        when(goodService.queryByName(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/goods/getGoodsByName")
                        .param("name", "Apple")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{\"gid\":1,\"name\":\"Apple\",\"description\":\"A sweet fruit\",\"location\":\"Fruit section\"},{\"gid\":2,\"name\":\"Apple Juice\",\"description\":\"A sweet drink\",\"location\":\"Beverage section\"}]}"));
    }

    @Test
    void testGetAll() throws Exception {
        Goods mockGoods1 = new Goods(1, "Apple", "A sweet fruit", "Fruit section");
        Goods mockGoods2 = new Goods(2, "Banana", "A tropical fruit", "Fruit section");
        Message<List<Goods>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(List.of(mockGoods1, mockGoods2));

        when(goodService.queryAll(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/goods/getAll")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{\"gid\":1,\"name\":\"Apple\",\"description\":\"A sweet fruit\",\"location\":\"Fruit section\"},{\"gid\":2,\"name\":\"Banana\",\"description\":\"A tropical fruit\",\"location\":\"Fruit section\"}]}"));
    }
}
