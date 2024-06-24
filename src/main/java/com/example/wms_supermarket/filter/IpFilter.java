package com.example.wms_supermarket.filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
@WebFilter(urlPatterns="/*")
//过滤恶意访问ip的过滤器，防爬虫
public class IpFilter implements Filter {
    //    恶意访问后的禁止访问时间
    private static final long bannedTime = 3600000;
    //    访问次数阈值
    private static final int threshold = 10;
    //    访问时间间隔最小值
    private static final int leastTime = 5000;
    private FilterConfig config;
    //    记录访问的ip表
    private static Map<String,Long[]> ipMap=new HashMap<String,Long[]>();
    //    记录被禁止访问的ip表
    private static Map<String, Long> bannedIpMap=new HashMap<String,Long>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;    //设置属性filterConfig
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        System.out.println("频率检测");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        refreshBannedIpMap(bannedIpMap);
        String ip = request.getRemoteAddr();

//        如果访问的ip是恶意ip，则拒绝访问
        if (isBannedIp(bannedIpMap, ip)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("您短时间内访问多次，已被登记为恶意ip，一个小时内禁止访问本网站。");
            return;
        }
        if (ipMap.containsKey(ip)) {
            Long[] ipInfo = ipMap.get(ip);
            ipInfo[0] = ipInfo[0] + 1;
            if (ipInfo[0] > threshold) {
                Long ipAccessTime = ipInfo[1];
                Long currentTimeMillis = System.currentTimeMillis();
//                如果访问的ip在本次访问后在最小时间以内大于了次数阈值，则将其认定为恶意ip
                if (currentTimeMillis - ipAccessTime <= leastTime) {
                    bannedIpMap.put(ip, currentTimeMillis + bannedTime);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println("您短时间内访问多次，已被登记为恶意ip，一个小时内禁止访问本网站。");
                    System.out.println("计入黑名单");
                    return;
                } else {
//                    达到访问次数的ip，时间大于最小时间，是合法ip
                    initIpMap(ipMap, ip);
                }
            }
        } else {
            initIpMap(ipMap, ip);
            System.out.println("第一次访问该网站");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    private void refreshBannedIpMap(Map<String, Long> bannedIpMap) {
        if (bannedIpMap == null) {
            return;
        }
        Set<String> keys = bannedIpMap.keySet();
        Iterator<String> keyIt = keys.iterator();
        long currentTimeMillis = System.currentTimeMillis();
        while (keyIt.hasNext()) {
            long expireTimeMillis = bannedIpMap.get(keyIt.next());
            if (expireTimeMillis <= currentTimeMillis) {
//                禁止时间过了就放出来
                keyIt.remove();
            }
        }
    }
    private boolean isBannedIp(Map<String, Long> limitedIpMap, String ip) {
        if (limitedIpMap == null || ip == null) {
            // 没有被限制
            return false;
        }
        Set<String> keys = limitedIpMap.keySet();
        Iterator<String> keyIt = keys.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            if (key.equals(ip)) {
                // 被限制的IP
                return true;
            }
        }
        return false;
    }
    private void initIpMap(Map<String, Long[]> ipMap, String ip) {
        Long[] ipInfo = new Long[2];
        ipInfo[0] = 0L;// 访问次数
        ipInfo[1] = System.currentTimeMillis();// 初次访问时间
        ipMap.put(ip, ipInfo);
    }

}
