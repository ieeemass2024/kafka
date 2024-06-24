package com.example.wms_supermarket;

import jakarta.servlet.http.Cookie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class Util {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public static int generateId(){
        Random random = new Random();

        // 生成八位数的随机数
        int randomNumber = random.nextInt(90000000) + 10000000;

        System.out.println("生成的八位随机数为: " + randomNumber);
        return randomNumber;
    }
    public static Cookie getCookie(String name, String value){
        Cookie cookie = new Cookie(name, value);

        // 设置 Cookie 的其他属性
        cookie.setMaxAge(3600); // 设置 cookie 的过期时间，单位为秒
        cookie.setPath("/"); // 设置 cookie 的路径

        // 将 Cookie 添加到 HttpServletResponse 中
        return cookie;
    }
    public static String getCookieValue(Cookie[] cookies,String name){
        String cookieValue=null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }
    public static String calculateMD5(String input) {
        try {
            // 获取 MessageDigest 实例，指定算法为 MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = input.getBytes();

            // 计算MD5哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
