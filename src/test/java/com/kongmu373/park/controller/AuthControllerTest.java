package com.kongmu373.park.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongmu373.park.common.ValidEnum;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class AuthControllerTest {

    private MockMvc mvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    @Test
    void testAuthNotLogin() throws Exception {
        when(userService.selectByUserName(null)).thenReturn(null);
        ResultActions perform = mvc.perform(get("/auth"));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"status\":\"ok\",\"login\":false}", true));
    }

    @Test
    void testLogoutNotLogin() throws Exception {
        when(userService.selectByUserName(null)).thenReturn(null);
        MockHttpSession session = new MockHttpSession();

        ResultActions perform = mvc.perform(get("/auth/logout").session(session));
        System.out.println(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
        perform.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"fail\",\"msg\":\"用户尚未登录\"}", true));
    }


    @Test
    void testLoginAndLogoutAuth() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        when(userService.selectByUserName("user")).thenReturn(new User().setId(1).setUsername("user").setPassword("pwd"));
        ResultActions perform = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        perform = mvc.perform(get("/auth"));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"status\":\"ok\",\"data\":{\"id\":1,\"username\":\"user\"},\"login\":true}", true));
        perform = mvc.perform(get("/auth/logout"));
        System.out.println(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
        perform.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"ok\",\"msg\":\"注销成功\"}", true));
    }

    @Test
    void testLoginUserIsNull() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        when(userService.selectByUserName("user")).thenReturn(null);
        ResultActions perform = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform.andExpect(status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"fail\",\"msg\":\"用户不存在\"}"));


    }

    @Test
    void testLoginWrongPwd() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "wrongPwd");
        when(userService.selectByUserName("user")).thenReturn(new User().setId(1).setUsername("user"));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "wrongPwd", Collections.emptyList()))).thenThrow(BadCredentialsException.class);
        ResultActions perform2 = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        System.out.println(perform2.andReturn().getResponse().getContentAsString());
        perform2.andExpect(status().isOk());
        perform2.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"fail\",\"msg\":\"密码不正确\"}"));
    }


    @Test
    void testAllRegisterThrowValidException() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        testOneRegisterThrowValidException(params, ValidEnum.PASSWORD_LENGTH.getMsg());

        params.put("username", "dddddddddddsadsa");
        testOneRegisterThrowValidException(params, ValidEnum.USERNAME_LENGTH.getMsg());

        params.put("username", "$._1");
        testOneRegisterThrowValidException(params, ValidEnum.REQUIRE_CHAR.getMsg());

        params.put("username", "username");
        params.put("password", null);
        testOneRegisterThrowValidException(params, ValidEnum.PASSWORD_NOT_EMPTY.getMsg());

        params.put("username", null);
        testOneRegisterThrowValidException(params, ValidEnum.USERNAME_NOT_EMPTY.getMsg());
    }

    @Test
    void testRegisterBeValid() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "username");
        params.put("password", "password");
        when(userService.insert("username", "password"))
                .thenReturn(new User().setId(1).setUsername("username").setPassword("password"));
        ResultActions perform = mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        System.out.println(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
        perform.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"ok\",\"msg\":\"注册成功\",\"data\":{\"id\":1,\"username\":\"username\"}}", true));
        when(userService.insert("username", "password"))
                .thenThrow(DuplicateKeyException.class);
        ResultActions perform2 = mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        System.out.println(perform2.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
        perform2.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"ok\",\"msg\":\"用户已存在\",\"login\":false}", true));
    }

    private void testOneRegisterThrowValidException(Map<String, String> params, String msg) throws Exception {
        ResultActions perform = mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(params)));
        System.out.println(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
        perform.andExpect(MockMvcResultMatchers.content().json("{\"status\":\"fail\",\"msg\":\"" + msg + "\",\"login\":false}", true));
    }
}