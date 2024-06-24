package com.example.wms_supermarket.config;

import com.example.wms_supermarket.MyWebSocketClient;
import com.example.wms_supermarket.filter.IpFilter;
import com.example.wms_supermarket.interceptor.LoginInterceptor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.java_websocket.client.WebSocketClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//如果需要使用knife4j查看接口文档，请注视掉WebConfig中的所有内容
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns=new ArrayList<>();
        patterns.add("/userLogin");
        patterns.add("/superLogin");
        patterns.add("/registerUser");
        patterns.add("/getSign");
        patterns.add("/start");
        patterns.add("/api-docs");
        patterns.add("/api");
        patterns.add("/swagger-ui/**");
        patterns.add("/doc");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 需要拦截的接口路径
                .excludePathPatterns(patterns);
        // 不需要拦截的接口路径，比如登录接口
    }
    @Bean
    public FilterRegistrationBean<IpFilter> myFilterRegistration() {
        FilterRegistrationBean<IpFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new IpFilter());
        registration.addUrlPatterns("/*"); // 过滤器的URL模式，这里设置为 /* 表示拦截所有请求
        registration.setName("myFilter");
        registration.setOrder(1); // 过滤器的顺序，如果有多个过滤器，根据顺序依次执行
        return registration;
    }
//    @Bean
//    public WebSocketClient webSocketClient() {
//        try {
//            MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI("ws://127.0.0.1:8089/truck-server/12345678"));
//            webSocketClient.connect();
//            return webSocketClient;
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//@Bean
//public ProducerFactory<Object, Object> producerFactory() {
//    Map<String, Object> config = new HashMap<>();
//    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//    // 其他的Kafka配置
//
//    return new DefaultKafkaProducerFactory<>(config);
//}
//
//    @Bean
//    public KafkaTemplate<Object, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }

}
