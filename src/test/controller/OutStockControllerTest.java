package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.OutStockTable;
import com.example.wms_supermarket.service.impl.OutStockTableServiceImpl;
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

public class OutStockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OutStockTableServiceImpl outStockTableService;

    @InjectMocks
    private OutStockController outStockController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(outStockController).build();
    }

    @Test
    public void testGetAllTable() throws Exception {
        OutStockTable table = new OutStockTable();
        Message<List<OutStockTable>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(Collections.singletonList(table));

        when(outStockTableService.queryAll(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/getAllTable")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{}]}"));
    }

    @Test
    public void testGetTableByStatus() throws Exception {
        OutStockTable table = new OutStockTable();
        Message<List<OutStockTable>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(Collections.singletonList(table));

        when(outStockTableService.queryByStatus(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/getTableByStatus")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{}]}"));
    }

    @Test
    public void testGetTableByTid() throws Exception {
        OutStockTable table = new OutStockTable();
        Message<OutStockTable> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(table);

        when(outStockTableService.queryByTid(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/getTableByTid")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":{}}"));
    }

    @Test
    public void testGetTableByGid() throws Exception {
        OutStockTable table = new OutStockTable();
        Message<List<OutStockTable>> mockMessage = new Message<>();
        mockMessage.setCode(0);
        mockMessage.setMsg("Success");
        mockMessage.setData(Collections.singletonList(table));

        when(outStockTableService.queryByGid(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/getTableByGid")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Success\",\"data\":[{}]}"));
    }

    @Test
    public void testInsertTable() throws Exception {
        Message mockMessage = new Message();
        mockMessage.setCode(0);
        mockMessage.setMsg("Inserted successfully");

        when(outStockTableService.insertOutTable(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/insertTable")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Inserted successfully\"}"));
    }

    @Test
    public void testGrantTable() throws Exception {
        Message mockMessage = new Message();
        mockMessage.setCode(0);
        mockMessage.setMsg("Granted successfully");

        when(outStockTableService.grantOutTable(any())).thenReturn(mockMessage);

        mockMvc.perform(get("/out/grantTable")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"code\":0,\"msg\":\"Granted successfully\"}"));
    }
}
