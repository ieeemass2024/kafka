package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.entity.Login;
import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.service.impl.LoginServiceImpl;
import com.example.wms_supermarket.service.impl.SuperManagerServiceImpl;
import com.example.wms_supermarket.service.impl.UserServiceImpl;
import static org.mockito.Mockito.doNothing;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class BasicConfigControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private LoginServiceImpl loginService;

    @Mock
    private SuperManagerServiceImpl superManagerService;

    @InjectMocks
    private BasicConfigController basicConfigController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(basicConfigController).build();
    }

    @Test
    void testGetSign() throws Exception {
        int nonce = 12345;
        int timestamp = 67890;
        int pwd = 11111;
        String expectedSign = Util.calculateMD5(nonce + "" + timestamp + pwd);

        mockMvc.perform(post("/getSign")
                        .param("nonce", String.valueOf(nonce))
                        .param("timestamp", String.valueOf(timestamp))
                        .param("pwd", String.valueOf(pwd)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedSign));
    }

    @Test
    void testUserLogin() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setPassword("password");
        mockUser.setIsRegister(1);
        when(userService.queryById(1)).thenReturn(mockUser);

        mockMvc.perform(post("/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"password\":\"password\"}")
                        .header("timestamp", "67890")
                        .header("nonce", "12345")
                        .header("sign", Util.calculateMD5("1234567890password")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":0,\"msg\":\"登录成功\"}"));
    }

    @Test
    void testGetUserById() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setName("testuser");
        when(userService.queryById(1)).thenReturn(mockUser);

        mockMvc.perform(post("/getUserById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userId\":1,\"name\":\"testuser\"}"));
    }

    @Test
    void testAddUser() throws Exception {
        mockMvc.perform(post("/addUser")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"msg\":\"添加成功\"}"));
    }

    @Test
    void testRegisterUser() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setRole("1");
        mockUser.setSuperId(1);
        when(superManagerService.queryById(1)).thenReturn(new SuperManager());

        mockMvc.perform(post("/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"role\":\"1\", \"superId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"msg\":\"成功提交注册信息，请等待管理员通过\",\"code\":0}"));
    }

    @Test
    void testSuperLogin() throws Exception {
        SuperManager mockManager = new SuperManager();
        mockManager.setPassword("password");
        when(superManagerService.queryById(1)).thenReturn(mockManager);

        mockMvc.perform(post("/superLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"password\":\"password\"}")
                        .header("timestamp", "67890")
                        .header("nonce", "12345")
                        .header("sign", Util.calculateMD5("1234567890password")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":0,\"msg\":\"登录成功\"}"));
    }

    @Test
    void testGetSuperById() throws Exception {
        SuperManager mockManager = new SuperManager();
        mockManager.setSuperId(1); // Ensure the SuperManager has the correct ID
        mockManager.setName("superadmin");
        when(superManagerService.queryById(1)).thenReturn(mockManager);

        mockMvc.perform(post("/getSuperById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"superId\":1,\"name\":\"superadmin\"}"));
    }

    @Test
    void testGetRegisterList() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        when(userService.queryByRegister(any(int.class))).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/getRegisterList")
                        .cookie(new Cookie("superId", "1")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userId\":1}]"));
    }

    @Test
    void testGetAll() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        when(userService.queryAll(any(int.class))).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/getAll")
                        .cookie(new Cookie("superId", "1")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userId\":1}]"));
    }



}
