package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.Goods;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.mapper.GoodsMapper;
import com.example.wms_supermarket.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class GoodsServiceImplTest {

    @InjectMocks
    private GoodServiceImpl goodsService;

    @Mock
    private GoodsMapper goodsMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryById() {
        // Set up HTTP request simulation
        when(httpServletRequest.getParameter("gid")).thenReturn("1");
        Goods goods = new Goods(1, "Apple", "Fresh Apples", "Storage");
        when(goodsMapper.queryById(1)).thenReturn(goods);

        Message<Goods> result = goodsService.queryById(httpServletRequest);
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(goods, result.getData());
    }
    @Test
    public void testQueryByName() {
        when(httpServletRequest.getParameter("name")).thenReturn("Apple");
        List<Goods> goodsList = List.of(new Goods(1, "Apple", "Fresh Apples", "Storage"));
        when(goodsMapper.queryByName("Apple")).thenReturn(goodsList);

        Message<List<Goods>> result = goodsService.queryByName(httpServletRequest);
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(goodsList, result.getData());
    }

    @Test
    public void testQueryAll()  {
        List<Goods> goodsList = List.of(new Goods(1, "Apple", "Fresh Apples", "Storage"));
        when(goodsMapper.queryAll()).thenReturn(goodsList);

        Message<List<Goods>> result = null;
        try {
            result = goodsService.queryAll(httpServletRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(goodsList, result.getData());
    }

    @Test
    public void testInsert() {
        // Arrange
        when(httpServletRequest.getParameter("name")).thenReturn("Apple");
        when(httpServletRequest.getParameter("description")).thenReturn("Fresh Apples");

        try (MockedStatic<Util> mockedUtil = Mockito.mockStatic(Util.class)) {
            mockedUtil.when(Util::generateId).thenReturn(100);
            Goods goods = new Goods(100, "Apple", "Fresh Apples", "还未入库");

            // Act
            when(goodsMapper.insertGoods(anyInt(), anyString(), anyString(), anyString())).thenReturn(1);
            Message<Goods> result = goodsService.insert(httpServletRequest);

            // Assert
            assertEquals(0, result.getCode());
            assertEquals("插入成功", result.getMsg());
            assertEquals(goods, result.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
