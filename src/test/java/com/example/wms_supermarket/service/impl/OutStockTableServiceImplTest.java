package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.OutStockTable;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.mapper.OutStockTableMapper;
import com.example.wms_supermarket.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class OutStockTableServiceImplTest {

    @Mock
    private OutStockTableMapper outStockTableMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private OutStockTableServiceImpl outStockTableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void setupOAuthMock(String userId, String role) {
        Cookie cookie = new Cookie("userId", userId);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        User user = new User();

        user.setRole(role);
        when(userMapper.queryById(Integer.parseInt(userId))).thenReturn(user);
    }

    @Test
    void testQueryAll_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        List<OutStockTable> expectedList = new ArrayList<>();
        when(outStockTableMapper.queryAll()).thenReturn(expectedList);

        // Act
        Message<List<OutStockTable>> result = outStockTableService.queryAll(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedList, result.getData());
        verify(outStockTableMapper, times(1)).queryAll();
    }

    @Test
    void testQueryByTid_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        OutStockTable expectedTable = new OutStockTable();
        when(httpServletRequest.getParameter("tid")).thenReturn("1");
        when(outStockTableMapper.queryByTid(anyInt())).thenReturn(expectedTable);

        // Act
        Message<OutStockTable> result = outStockTableService.queryByTid(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedTable, result.getData());
        verify(outStockTableMapper, times(1)).queryByTid(1);
    }

    @Test
    void testQueryByGid_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        List<OutStockTable> expectedList = new ArrayList<>();
        when(httpServletRequest.getParameter("gid")).thenReturn("1");
        when(outStockTableMapper.queryByGid(anyInt())).thenReturn(expectedList);

        // Act
        Message<List<OutStockTable>> result = outStockTableService.queryByGid(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedList, result.getData());
        verify(outStockTableMapper, times(1)).queryByGid(1);
    }

    @Test
    void testQueryByStatus_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        List<OutStockTable> expectedList = new ArrayList<>();
        when(httpServletRequest.getParameter("status")).thenReturn("1");
        when(outStockTableMapper.queryByStatus(anyInt())).thenReturn(expectedList);

        // Act
        Message<List<OutStockTable>> result = outStockTableService.queryByStatus(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(expectedList, result.getData());
        verify(outStockTableMapper, times(1)).queryByStatus(1);
    }

    @Test
    void testInsertOutTable_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        when(httpServletRequest.getParameter("gid")).thenReturn("1");

        // Act
        Message result = outStockTableService.insertOutTable(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("插入成功", result.getMsg());
        verify(outStockTableMapper, times(1)).insertOutTable(anyInt(), anyInt());
    }

    @Test
    void testGrantOutTable_Success() {
        // Arrange
        setupOAuthMock("1", "1"); // Mock user with valid role
        when(httpServletRequest.getParameter("tid")).thenReturn("1");

        // Act
        Message result = outStockTableService.grantOutTable(httpServletRequest);

        // Assert
        assertEquals(0, result.getCode());
        assertEquals("插入成功", result.getMsg());
        verify(outStockTableMapper, times(1)).grantOutTable(anyInt());
    }

    @Test
    void testQueryAll_NoAccess() {
        // Arrange
        setupOAuthMock("1", "3"); // Mock user with no access role

        // Act
        Message<List<OutStockTable>> result = outStockTableService.queryAll(httpServletRequest);

        // Assert
        assertNotEquals(0, result.getCode());
        assertEquals("当前身份无权限查看", result.getMsg());
        verify(outStockTableMapper, never()).queryAll();
    }
}
