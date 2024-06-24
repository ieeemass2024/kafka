package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.entity.Login;
import com.example.wms_supermarket.mapper.LoginMapper;
import com.example.wms_supermarket.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceImplTest {

    @Mock
    private LoginMapper loginMapper;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryByNonce_Success() {
        // Arrange
        int nonce = 12345;
        Login expectedLogin = new Login();
        expectedLogin.setNonce(nonce);
        when(loginMapper.queryByNonce(anyInt())).thenReturn(expectedLogin);

        // Act
        Login result = loginService.queryByNonce(nonce);

        // Assert
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        verify(loginMapper, times(1)).queryByNonce(nonce);
    }

    @Test
    void testQueryByNonce_NotFound() {
        // Arrange
        int nonce = 12345;
        when(loginMapper.queryByNonce(anyInt())).thenReturn(null);

        // Act
        Login result = loginService.queryByNonce(nonce);

        // Assert
        assertNull(result);
        verify(loginMapper, times(1)).queryByNonce(nonce);
    }

    @Test
    void testInsert_Success() {
        // Arrange
        Login login = new Login();
        login.setNonce(12345);
        when(loginMapper.insert(any(Login.class))).thenReturn(1);

        // Act
        int result = loginService.insert(login);

        // Assert
        assertEquals(1, result);
        verify(loginMapper, times(1)).insert(login);
    }

    @Test
    void testInsert_Failure() {
        // Arrange
        Login login = new Login();
        login.setNonce(12345);
        when(loginMapper.insert(any(Login.class))).thenReturn(0);

        // Act
        int result = loginService.insert(login);

        // Assert
        assertEquals(0, result);
        verify(loginMapper, times(1)).insert(login);
    }
}
