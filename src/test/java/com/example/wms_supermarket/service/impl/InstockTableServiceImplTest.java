package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.entity.InStockTable;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.mapper.InStockTableMapper;
import com.example.wms_supermarket.mapper.UserMapper;
import com.example.wms_supermarket.mapper.GoodsMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class InstockTableServiceImplTest {

    @Mock
    private InStockTableMapper inStockTableMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private GoodsMapper goodsMapper;

    @InjectMocks
    private InstockTableServcieImpl instockTableService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAll_Success() {
        // Arrange
        List<InStockTable> expectedList = new ArrayList<>();
        when(inStockTableMapper.queryAll()).thenReturn(expectedList);
        when(httpServletRequest.getCookies()).thenReturn(null); // Mock cookies for oauth

        // Act
        Message<List<InStockTable>> result = instockTableService.queryAll(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedList, result.getData());
        verify(inStockTableMapper, times(1)).queryAll();
    }

    @Test
    void testQueryByTid_Success() {
        // Arrange
        InStockTable expectedTable = new InStockTable();
        when(inStockTableMapper.queryByTid(anyInt())).thenReturn(expectedTable);
        when(httpServletRequest.getParameter("tid")).thenReturn("1");
        when(httpServletRequest.getCookies()).thenReturn(null); // Mock cookies for oauth

        // Act
        Message<InStockTable> result = instockTableService.queryByTid(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedTable, result.getData());
        verify(inStockTableMapper, times(1)).queryByTid(1);
    }

    @Test
    void testQueryByGid_Success() {
        // Arrange
        List<InStockTable> expectedList = new ArrayList<>();
        when(inStockTableMapper.queryByGid(anyInt())).thenReturn(expectedList);
        when(httpServletRequest.getParameter("gid")).thenReturn("1");
        when(httpServletRequest.getCookies()).thenReturn(null); // Mock cookies for oauth

        // Act
        Message<List<InStockTable>> result = instockTableService.queryByGid(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedList, result.getData());
        verify(inStockTableMapper, times(1)).queryByGid(1);
    }

    @Test
    void testInsertInTable_Success() {
        // Arrange
        int gid = 1;

        // Act
        Message result = instockTableService.insertInTable(gid);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("插入成功", result.getMsg());
        verify(inStockTableMapper, times(1)).insertInTable(anyInt(), eq(gid));
    }

    @Test
    void testCompleteInTable_Success() {
        // Arrange
        int tid = 1;

        // Act
        Message result = instockTableService.completeInTable(tid);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("货物入库", result.getMsg());
        verify(inStockTableMapper, times(1)).completeInTable(tid);
    }
}
