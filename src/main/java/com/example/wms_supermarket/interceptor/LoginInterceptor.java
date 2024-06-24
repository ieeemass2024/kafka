package com.example.wms_supermarket.interceptor;

import com.example.wms_supermarket.entity.Message;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
//拦截没有token的登录行为
public class LoginInterceptor implements HandlerInterceptor {
    public static HashMap<Integer, String> hashMap = new HashMap<Integer,String>();
    public Boolean isLogin(HttpServletRequest request) {
        String cookieValue = null;
        int id=0;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    cookieValue = cookie.getValue();
                    System.out.println(cookieValue);
                }
                else if (cookie.getName().equals("userId")) {
                    id = Integer.parseInt(cookie.getValue());
                    System.out.println("user"+id);
                }
                else if (cookie.getName().equals("superId")) {
                    id = Integer.parseInt(cookie.getValue());
                    System.out.println("super"+id);
                }
            }
        }

        if (cookieValue==null||id==0)
            return false;
        else if(hashMap.get(id)==null){
            return false;
        }
        else if(!hashMap.get(id).equals(cookieValue))
            return false;
        else
            return true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在这里进行用户登录验证的逻辑，如果验证通过则返回 true，否则返回 false
        Boolean islogin=false;
        // 假设这里只是简单判断用户是否已经登录
        if (isLogin(request)) {
            return true;
        } else {
            Message msg=new Message();
            msg.setCode(101);
            msg.setMsg("还未登录");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(msg.toJson());
            printWriter.flush();
            printWriter.close();
            return false;
        }
    }
}
