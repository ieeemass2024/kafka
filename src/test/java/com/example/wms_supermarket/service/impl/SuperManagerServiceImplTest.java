package com.example.wms_supermarket.service.impl;

import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.mapper.SuperManagerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SuperManagerServiceImplTest {

    @Mock
    private SuperManagerMapper superManagerMapper;

    @InjectMocks
    private SuperManagerServiceImpl superManagerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryById() {
        SuperManager mockManager = new SuperManager();
        mockManager.setSuperId(1);
        mockManager.setName("testName");
        mockManager.setPassword("testPassword");
        mockManager.setRealName("Test Real Name");

        when(superManagerMapper.queryById(1)).thenReturn(mockManager);

        SuperManager result = superManagerService.queryById(1);
        assertEquals(mockManager, result);
    }

    @Test
    public void testQueryBySuperId() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setName("user1");
        user1.setSuperId(1);
        user1.setRole("user");
        user1.setIsRegister(1);

        User user2 = new User();
        user2.setUserId(2);
        user2.setName("user2");
        user2.setSuperId(1);
        user2.setRole("user");
        user2.setIsRegister(1);

        List<User> userList = Arrays.asList(user1, user2);

        when(superManagerMapper.queryBySuperId(1)).thenReturn(userList);

        List<User> result = superManagerService.queryBySuperId(1);
        assertEquals(userList, result);
    }
}
