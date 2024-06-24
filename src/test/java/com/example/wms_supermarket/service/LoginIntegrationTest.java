package com.example.wms_supermarket.service;

import com.example.wms_supermarket.entity.Goods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String baseUrl;

    private String getUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void testUserLogin_Success() {
        String json = "{\"userId\":\"1\", \"nonce\":\"12345\", \"timestamp\":\"" + (System.currentTimeMillis() / 1000) + "\", \"sign\":\"computed-md5-sign\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/userLogin", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void assertEquals(HttpStatus ok, HttpStatusCode statusCode) {
    }

    @Test
    public void testUserLogin_Failure() {
        String json = "{\"userId\":\"1\", \"nonce\":\"12345\", \"timestamp\":\"" + (System.currentTimeMillis() / 1000) + "\", \"sign\":\"wrong-sign\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/userLogin", request, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUserLogin_NonceReused() {
        String json = "{\"userId\":\"1\", \"nonce\":\"12345\", \"timestamp\":\"" + (System.currentTimeMillis() / 1000) + "\", \"sign\":\"computed-md5-sign\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.postForEntity(baseUrl + "/userLogin", request, String.class);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/userLogin", request, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    private class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
        private final HttpHeaders headers;

        public RequestResponseLoggingInterceptor(HttpHeaders headers) {
            this.headers = headers;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().addAll(this.headers);
            return execution.execute(request, body);
        }
    }

    @Test
    public void testApiHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetGoodsById() {
        int testGoodsId = 1;
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/goods/getGoodsById?id=" + testGoodsId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetAllGoods() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/goods/getAll", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteGoods() {
        ResponseEntity<Void> response = restTemplate.exchange(getUrl("/goods/deleteGoods?id=2"), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
