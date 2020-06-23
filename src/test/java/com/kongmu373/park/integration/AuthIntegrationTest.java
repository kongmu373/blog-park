package com.kongmu373.park.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongmu373.park.Application;
import com.kongmu373.park.common.ValidEnum;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Value("${local.server.port}")
    String port;

    @Test
    public void notLoggedInByDefault() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            testOneGet(httpClient, "/auth", "false");

        }
    }

    @Test
    public void testLogoutNotLogin() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            testOneGet(httpClient, "/auth/logout", "用户尚未登录");
        }
    }

    @Test
    public void testLoginAndLogoutAuth() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            Map<String, String> params = new HashMap<>();
            params.put("username", "username3");
            params.put("password", "123456");
            testOnePost(params, httpClient, "/auth/register", "注册成功");
            testOnePost(params, httpClient, "/auth/login", "登录成功");
            testOneGet(httpClient, "/auth", "ok");
            testOneGet(httpClient, "/auth/logout", "注销成功");
        }
    }


    @Test
    public void testAllRegister() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            Map<String, String> params = new HashMap<>();
            params.put("username", "username3");
            params.put("password", "123456");
            testOnePost(params, httpClient, "/auth/register", "用户已存在");
            params.put("username", "dsaaaaaaaaaadsadsadsadas");
            testOnePost(params, httpClient, "/auth/register", ValidEnum.USERNAME_LENGTH.getMsg());
            params.put("username", "$.");
            testOnePost(params, httpClient, "/auth/register", ValidEnum.REQUIRE_CHAR.getMsg());
            params.put("username", null);
            testOnePost(params, httpClient, "/auth/register", ValidEnum.USERNAME_NOT_EMPTY.getMsg());
            params.put("username", "normalusername");
            params.put("password", "12");
            testOnePost(params, httpClient, "/auth/register", ValidEnum.PASSWORD_LENGTH.getMsg());
            params.put("password", null);
            testOnePost(params, httpClient, "/auth/register", ValidEnum.PASSWORD_NOT_EMPTY.getMsg());
            params.put("username", "newusertdc");
            params.put("password", "123456");
            testOnePost(params, httpClient, "/auth/register", "注册成功");
        }
    }

    private void testOneGet(CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpGet get = new HttpGet("http://localhost:" + port + path);
        CloseableHttpResponse authResponse = httpClient.execute(get);
        Assertions.assertEquals(200, authResponse.getStatusLine().getStatusCode());
        Assertions.assertTrue(EntityUtils.toString(authResponse.getEntity()).contains(statement));

    }

    private void testOnePost(Map<String, String> params, CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpPost post = new HttpPost("http://localhost:" + port + path);
        String duplicateBody = new ObjectMapper().writeValueAsString(params);
        post.setEntity(new StringEntity(duplicateBody, StandardCharsets.UTF_8));
        post.setHeader("Content-type", "application/json");
        CloseableHttpResponse DuplicateResponse = httpClient.execute(post);
        Assertions.assertEquals(200, DuplicateResponse.getStatusLine().getStatusCode());
        String s = EntityUtils.toString(DuplicateResponse.getEntity());
        System.out.println(s);
        Assertions.assertTrue(s.contains(statement));
    }
}
