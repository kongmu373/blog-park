package com.kongmu373.park.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongmu373.park.Application;
import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.common.ValidEnum;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
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
import java.util.LinkedHashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

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
            params.put("username", "kongmu12");
            params.put("password", "123456");
            testOnePost(params, httpClient, "/auth/register", "注册成功");
        }
    }

    @Test
    public void testCreateAndUpdateAndDeleteBlog() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            Map<String, String> blogParam = new HashMap<>();
            blogParam.put("title", "create_title");
            blogParam.put("content", "create_content");
            testOnePost(blogParam, httpClient, "/blog", "登录后才能操作");
            Map<String, String> loginParam = new HashMap<>();
            loginParam.put("username", "kongmu12");
            loginParam.put("password", "123456");
            testOnePost(loginParam, httpClient, "/auth/login", "登录成功");
            String create_common_result = testOnePost(blogParam, httpClient, "/blog", "create_title");
            CommonResult<LinkedHashMap> commonResult = new ObjectMapper().readValue(create_common_result, CommonResult.class);
            System.out.println(">>>>>>>>>>>" + commonResult);
            Integer id = (Integer) commonResult.getData().get("id");
            Map<String, String> updateParam = new HashMap<>();
            updateParam.put("title", "update_title");
            updateParam.put("content", "update_content");
            testOnePatch(updateParam, httpClient, "/blog/" + id, "修改成功");
            testOneDelete(httpClient, "/blog/" + id, "删除成功");
            testOneDelete(httpClient, "/blog/9999", "博客不存在");
            testOneGet(httpClient, "/auth/logout", "注销成功");
        }
    }

    @Test
    public void testGetBlogById() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            testOneGet(httpClient, "/blog/1", "create_title");
        }
    }

    @Test
    public void testGetBlogs() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            Map<String, String> loginParam = new HashMap<>();
            loginParam.put("username", "username3");
            loginParam.put("password", "123456");
            testOnePost(loginParam, httpClient, "/auth/login", "登录成功");
            Map<String, String> blogParam = new HashMap<>();
            blogParam.put("title", "create_title");
            blogParam.put("content", "create_content");
            for (int i = 0; i < 10; i++) {
                testOnePost(blogParam, httpClient, "/blog", "创建成功");
            }
            testOneGet(httpClient, "/blog?page=1", "获取成功");
            testOneGet(httpClient, "/auth/logout", "注销成功");
        }

    }

    private void testOnePatch(Map<String, String> params, CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpPatch patch = new HttpPatch("http://localhost:" + port + path);
        String duplicateBody = new ObjectMapper().writeValueAsString(params);
        patch.setEntity(new StringEntity(duplicateBody, StandardCharsets.UTF_8));
        patch.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(patch);
        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
        String s = EntityUtils.toString(response.getEntity());
        System.out.println(s);
        Assertions.assertTrue(s.contains(statement));
    }

    private void testOneGet(CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpGet get = new HttpGet("http://localhost:" + port + path);
        CloseableHttpResponse authResponse = httpClient.execute(get);
        String s = EntityUtils.toString(authResponse.getEntity());
        System.out.println(s);
        Assertions.assertEquals(200, authResponse.getStatusLine().getStatusCode());
        Assertions.assertTrue(s.contains(statement));

    }

    private void testOneDelete(CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpDelete delete = new HttpDelete("http://localhost:" + port + path);
        CloseableHttpResponse authResponse = httpClient.execute(delete);
        Assertions.assertEquals(200, authResponse.getStatusLine().getStatusCode());
        String s = EntityUtils.toString(authResponse.getEntity());
        Assertions.assertTrue(s.contains(statement));

    }

    private String testOnePost(Map<String, String> params, CloseableHttpClient httpClient, String path, String statement) throws Exception {
        HttpPost post = new HttpPost("http://localhost:" + port + path);
        String duplicateBody = new ObjectMapper().writeValueAsString(params);
        post.setEntity(new StringEntity(duplicateBody, StandardCharsets.UTF_8));
        post.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);
        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
        String s = EntityUtils.toString(response.getEntity());
        System.out.println(s);
        Assertions.assertTrue(s.contains(statement));
        return s;
    }
}
