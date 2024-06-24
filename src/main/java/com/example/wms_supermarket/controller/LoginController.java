package com.example.wms_supermarket.controller;

import com.example.wms_supermarket.Util;
import com.example.wms_supermarket.entity.Login;
import com.example.wms_supermarket.interceptor.LoginInterceptor;
import com.example.wms_supermarket.entity.Message;
import com.example.wms_supermarket.entity.SuperManager;
import com.example.wms_supermarket.entity.User;
import com.example.wms_supermarket.service.UserService;
import com.example.wms_supermarket.service.impl.LoginServiceImpl;
import com.example.wms_supermarket.service.impl.SuperManagerServiceImpl;
import com.example.wms_supermarket.service.impl.UserServiceImpl;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.alibaba.fastjson.JSONObject;

@RestController
@Tag(name = "BasicConfigController", description = "管理人员信息的接口")
public class LoginController {
    @Autowired
    private SuperManagerServiceImpl superManagerService;
    @Autowired
    private UserServiceImpl userService;
    //生成唯一token
    @Autowired
    private LoginServiceImpl loginService;
    @PostMapping("/getSign")
    public ResponseEntity<String> getSign(@RequestParam(value = "nonce") int nonce, @RequestParam(value = "timestamp") int timestamp, @RequestParam(value = "pwd") int pwd) {
        return ResponseEntity.ok(Util.calculateMD5(nonce+""+timestamp+pwd));
    }

    //用户登录
    @Operation(summary = "登录", description = "登录")
    @Parameters({
            @Parameter(name = "userId", required = true,description="用户的id"),
            @Parameter(in = ParameterIn.HEADER,name = "nonce",required = true,description="登录的挑战，随机数"),
            @Parameter(in = ParameterIn.HEADER,name = "timestamp",required = true,description="生成的时间戳，防止重放攻击"),
            @Parameter(in = ParameterIn.HEADER,name = "sign",required = true,description="用户密码、挑战和时间戳一起生成的安全散列函数值"),
    })
    @ApiResponse(description = "登录的结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    @PostMapping(path = "/userLogin")
    public void userLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        System.out.println(111);
        int id=0;
        String password=null;
        Message msg =new Message();
        try {
            String timestamp = httpServletRequest.getHeader("timestamp");
            // 随机数
            String nonce = httpServletRequest.getHeader("nonce");
            // 签名算法生成的签名
            String sign = httpServletRequest.getHeader("sign");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            if (timestamp==null||nonce==null||sign==null){
                System.out.println("请求头不可以为空");
                msg.setCode(102);
                msg.setMsg("请求头不可用");
                httpServletResponse.getWriter().println(msg.toJson());
                httpServletResponse.getWriter().close();
                return;
            }
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = httpServletRequest.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String requestData = buffer.toString();
            // 解析 JSON 数据
            JSONObject json = JSONObject.parseObject(requestData);
            String userId = json.getString("userId");
            id =Integer.parseInt(userId);
            System.out.println(userId);
            User user = userService.queryById(id);
            String pwd=user.getPassword();
            String input=nonce+""+timestamp+pwd;
            String md5=Util.calculateMD5(input);
//            如果用户的登录信息已经被记录在数据库中（每五分钟删除一次），则认定为重复登录
            if(loginService.queryByNonce(Integer.parseInt(nonce))!=null){
                msg.setCode(1);
                msg.setMsg("不可重复登录");
                System.out.println(111);
            }
            else if (user==null){
                msg.setCode(1);
                msg.setMsg("该用户不存在");
            }
            else if (user.getIsRegister()==0){
                msg.setCode(1);
                msg.setMsg("该用户不存在");
            }
//            密码正确（用户真实密码与用户使用的挑战、时间戳生成的安全散列函数值与sign相同）
            else if(md5.equals(sign)){
                String token= Util.generateToken();
                loginService.insert(new Login(Integer.parseInt(nonce),Integer.parseInt(timestamp)));
                httpServletResponse.addCookie(Util.getCookie("token",token));
                httpServletResponse.addCookie(Util.getCookie("userId",id+""));
                msg.setCode(0);
                msg.setMsg("登录成功");
                LoginInterceptor.hashMap.put(id,token);
            }
            else{
                msg.setCode(1);
                msg.setMsg("登录失败");
            }
            PrintWriter printWriter = null;
            printWriter = httpServletResponse.getWriter();
            printWriter.write(msg.toJson());
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            // 处理 IOException 异常
            e.printStackTrace();
        }
    }

    @Operation(summary = "获取特定id的用户", description = "获取特定id的用户")
    @Parameters({
            @Parameter(name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "特定id的用户的信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    //获取用户信息
    @PostMapping(path = "/getUserById")
    public User getUserById(int id){
        return userService.queryById(id);
    }

    @Operation(summary = "超管通过用户的注册", description = "超管通过用户的注册")
    @Parameters({
            @Parameter(name = "userId", required = true, description = "需要通过注册的用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "通过注册的结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    //超管通过用户的注册
    @PostMapping(path = "/addUser")
    public Message addUser(@RequestParam int id){
        Message msg=new Message();
        userService.addUser(id);
        msg.setMsg("添加成功");
//        msg.setCode(0);
        return msg;
    }

    @Operation(summary = "用户注册", description = "用户注册" )
    @ApiResponse(description = "通过注册的结果",content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    //添加用户
    @PostMapping(path = "/registerUser")
    public Message registerUser(@RequestBody User user){
        Message msg=new Message();
        if (!(user.getRole().equals("0")||user.getRole().equals("1")||user.getRole().equals("2")||user.getRole().equals("3"))){
            msg.setMsg("职位码出错");
            msg.setCode(1);
        }
        else if(superManagerService.queryById(user.getSuperId())==null){
            msg.setMsg("超管"+user.getSuperId()+"不存在");
            msg.setCode(1);
        }
        int id=0;
        while(true){
            id=Util.generateId();
            if (userService.queryById(id)==null)
                break;
        }
        user.setUserId(id);
        user.setIsRegister(0);
        userService.registerUser(user);
        msg.setMsg("成功提交注册信息，请等待管理员通过");
        msg.setCode(0);
        return msg;
    }

    @Operation(summary = "超管登录", description = "超管登录")
    @Parameters({
            @Parameter(name = "superId", required = true,description="超管的id"),
            @Parameter(in = ParameterIn.HEADER,name = "nonce",required = true,description="登录的挑战，随机数"),
            @Parameter(in = ParameterIn.HEADER,name = "timestamp",required = true,description="生成的时间戳，防止重放攻击"),
            @Parameter(in = ParameterIn.HEADER,name = "sign",required = true,description="用户密码、挑战和时间戳一起生成的安全散列函数值"),
    })
    @ApiResponse(description = "登录的结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    //超管登录
    @PostMapping(path = "/superLogin")
    public void superLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        System.out.println(111);
        int id=0;
        Message msg =new Message();
        try {
            String timestamp = httpServletRequest.getHeader("timestamp");
            // 随机数
            String nonce = httpServletRequest.getHeader("nonce");
            // 签名算法生成的签名
            String sign = httpServletRequest.getHeader("sign");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            if (timestamp==null||nonce==null||sign==null){
                System.out.println("请求头不可以为空");
                msg.setCode(102);
                msg.setMsg("请求头不可用");
                httpServletResponse.getWriter().println(msg.toJson());
                httpServletResponse.getWriter().close();
                return;
            }
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = httpServletRequest.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String requestData = buffer.toString();
            // 解析 JSON 数据
            JSONObject json = JSONObject.parseObject(requestData);
            String userId = json.getString("userId");
            id =Integer.parseInt(userId);
            System.out.println(userId);
            SuperManager superManager = superManagerService.queryById(id);
            String pwd=superManager.getPassword();
            String input=nonce+""+timestamp+pwd;
            String md5=Util.calculateMD5(input);
            //重复登录
            if(loginService.queryByNonce(Integer.parseInt(nonce))!=null){
                msg.setCode(1);
                msg.setMsg("不可重复登录");
                System.out.println(111);
            }
            else if (superManager==null){
                msg.setCode(1);
                msg.setMsg("该超管不存在");
            }
//            如果用户的登录信息已经被记录在数据库中（每五分钟删除一次），则认定为重复登录
            else if(md5.equals(sign)){
                String token= Util.generateToken();
                loginService.insert(new Login(Integer.parseInt(nonce),Integer.parseInt(timestamp)));
                httpServletResponse.addCookie(Util.getCookie("token",token));
                httpServletResponse.addCookie(Util.getCookie("superId",userId));
                msg.setCode(0);
                msg.setMsg("登录成功");
                LoginInterceptor.hashMap.put(id,token);
                System.out.println(LoginInterceptor.hashMap.keySet());
                System.out.println(LoginInterceptor.hashMap.values());
            }
            else{
                msg.setCode(1);
                msg.setMsg("登录失败");
            }
            PrintWriter printWriter = null;
            printWriter = httpServletResponse.getWriter();
            printWriter.write(msg.toJson());
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            // 处理 IOException 异常
            e.printStackTrace();
        }
    }

    @Operation(summary = "通过id获取超管的信息", description = "通过id获取超管的信息")
    @Parameters({
            @Parameter(name = "id", required = true, description = "想要获取的超管的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "通过id获取超管的信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuperManager.class)))
    //获取超管信息
    @PostMapping(path = "/getSuperById")
    public SuperManager getSuperById(@RequestParam int id){
        return superManagerService.queryById(id);
    }

    @Operation(summary = "超管查看自己名下的人员信息", description = "超管查看自己名下的人员信息")
    @Parameters({
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "自己名下的人员信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    //查看自己名下正在申请注册的名单
    @GetMapping(path = "/getRegisterList")
    public List<User> getRegisterList(HttpServletRequest httpServletRequest)
    {
        int id = Integer.parseInt(Util.getCookieValue(httpServletRequest.getCookies(),"superId"));
        return userService.queryByRegister(id);
    }

    @Operation(summary = "查看所有人员信息", description = "查看所有人员信息")
    @Parameters({
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "所有人员信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping(path = "/getAll")
    public List<User> getAll(HttpServletRequest httpServletRequest){
        System.out.println(666);
        int id = Integer.parseInt(Util.getCookieValue(httpServletRequest.getCookies(),"superId"));
        return userService.queryAll(id);
    }

    @Operation(summary = "删除某位人员信息", description = "删除某位人员信息")
    @Parameters({
            @Parameter(name = "id", required = true, description = "想要删除的超管的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "userId", required = true, description = "用户的id"),
            @Parameter(in = ParameterIn.COOKIE, name = "token", required = true, description = "用户的登录令牌"),
    })
    @ApiResponse(description = "删除的结果信息", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping(path = "/delete")
    public void deleteUser(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        String cookie=Util.getCookieValue(httpServletRequest.getCookies(),"superId");
        Message msg=new Message();
        if (cookie==null){
            msg.setCode(1);
            msg.setMsg("删除失败，无相应权限");
        }
        else{
            String cookie2=Util.getCookieValue(httpServletRequest.getCookies(),"token");
            if (cookie2==null){
                msg.setCode(1);
                msg.setMsg("删除失败，无相应权限");
            }
            else{
                int superId = Integer.parseInt(cookie);
                if (LoginInterceptor.hashMap.get(superId)==null||!LoginInterceptor.hashMap.get(superId).equals(cookie2)){
                    msg.setCode(1);
                    msg.setMsg("删除失败，无相应权限");
                }
                else{
                    int id=Integer.parseInt(httpServletRequest.getParameter("id"));
                    userService.delete(id);
                    msg.setCode(0);
                    msg.setMsg("删除成功");
                }
            }

        }
        PrintWriter printWriter = null;
        try {
            printWriter = httpServletResponse.getWriter();
            printWriter.write(msg.toJson());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
