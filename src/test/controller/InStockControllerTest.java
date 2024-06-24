package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.InStockTable;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.service.impl.GoodServiceImpl;
import com.example.wms_supermarket.service.impl.InstockTableServcieImpl;
import com.example.wms_supermarket.service.impl.RecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InStockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoodServiceImpl goodService;

    @Mock
    private InstockTableServcieImpl inStockTableService;

    @Mock
    private RecordServiceImpl recordService;

    @Mock
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @InjectMocks
    private InStockConrtoller inStockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inStockController).build();
    }

    @Test
    void testGetAllTable() throws Exception {
        InStockTable table = new InStockTable();
        Message<List<InStockTable>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(Collections.singletonList(table));

        when(inStockTableService.queryAll(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/getAllTable")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{}]}"));
    }

    @Test
    void testGetTableByStatus() throws Exception {
        InStockTable table = new InStockTable();
        Message<List<InStockTable>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(Collections.singletonList(table));

        when(inStockTableService.queryByStatus(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/getTableByStatus")
                        .param("status", "1")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{}]}"));
    }

    @Test
    void testGetTableByTid() throws Exception {
        InStockTable table = new InStockTable();
        Message<InStockTable> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(table);

        when(inStockTableService.queryByTid(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/getTableByTid")
                        .param("tid", "1")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":{}}"));
    }

    @Test
    void testInsertTable() throws Exception {
        Goods goods = new Goods(1, "Apple", "A sweet fruit", "Fruit section");
        Message<Goods> mockGoodsMessage = new Message<>();
        mockGoodsMessage.setCode(0);
        mockGoodsMessage.setMsg("Success");
        mockGoodsMessage.setData(goods);

        Message mockTableMessage = new Message();
        mockTableMessage.setCode(0);
        mockTableMessage.setMsg("Inserted successfully");

        when(goodService.insert(any())).thenReturn(mockGoodsMessage);
        when(inStockTableService.insertInTable(anyInt())).thenReturn(mockTableMessage);

        mockMvc.perform(get("/insertTable")
                        .param("gid", "1")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Inserted successfully\"}"));
    }

    @Test
    void testGrantTable() throws Exception {
        Message mockMessage = new Message();
        mockMessage.setCode(0);
        mockMessage.setMsg("Granted successfully");

        InStockTable table = new InStockTable();
        Message<InStockTable> mockTableMessage = new Message<>();
        mockTableMessage.setCode(0);
        mockTableMessage.setMsg("Success");
        mockTableMessage.setData(table);

        when(inStockTableService.grantInTable(any())).thenReturn(mockMessage);
        when(inStockTableService.queryByTid(any())).thenReturn(mockTableMessage);

        mockMvc.perform(get("/grantTable")
                        .param("tid", "1")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Granted successfully\"}"));

        verify(kafkaTemplate, times(1)).send("wms_to_shelves_topic_into_stock", mockTableMessage.toJson());
    }

    @Test
    void testSendFoo() throws Exception {
        mockMvc.perform(get("/send/test")
                        .accept("application/json"))
                .andExpect(status().isOk());

        verify(kafkaTemplate, times(1)).send("topic_input", "test");
    }
}
