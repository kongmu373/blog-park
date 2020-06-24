package com.kongmu373.park.controller;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.kongmu373.park.utils.TestUtils.testGet;
import static com.kongmu373.park.utils.TestUtils.testPost;
import static org.mockito.Mockito.when;

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
        when(userService.getCurrentUser()).thenReturn(Optional.empty());
        testGet(mvc, "/auth", "\"login\":false");
    }

    @Test
    void testLogoutNotLogin() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.empty());
        testGet(mvc, "/auth/logout", "用户尚未登录");
    }


    @Test
    void testLoginAndLogoutAuth() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        User user = new User().setId(1).setUsername("user").setPassword("pwd");
        when(userService.selectByUserName("user")).thenReturn(user);
        testPost(mvc, "/auth/login", params, "\"msg\":\"登录成功\"");
        when(userService.getCurrentUser()).thenReturn(Optional.ofNullable(user));
        testGet(mvc, "/auth", "\"data\":{\"id\":1,\"username\":\"user\"},\"login\":true");
        testGet(mvc, "/auth/logout", "注销成功");
    }

    @Test
    void testLoginUserIsNull() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        when(userService.selectByUserName("user")).thenReturn(null);
        testPost(mvc, "/auth/login", params, "用户不存在");
    }

    @Test
    void testLoginWrongPwd() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "wrongPwd");
        when(userService.selectByUserName("user")).thenReturn(new User().setId(1).setUsername("user"));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "wrongPwd", Collections.emptyList()))).thenThrow(BadCredentialsException.class);
        testPost(mvc, "/auth/login", params, "密码不正确");
    }


    @Test
    void testAllRegisterThrowValidException() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "pwd");
        testPost(mvc, "/auth/register", params, ValidEnum.PASSWORD_LENGTH.getMsg());

        params.put("username", "dddddddddddsadsa");
        testPost(mvc, "/auth/register", params, ValidEnum.USERNAME_LENGTH.getMsg());

        params.put("username", "$._1");
        testPost(mvc, "/auth/register", params, ValidEnum.REQUIRE_CHAR.getMsg());

        params.put("username", "username");
        params.put("password", null);
        testPost(mvc, "/auth/register", params, ValidEnum.PASSWORD_NOT_EMPTY.getMsg());

        params.put("username", null);
        testPost(mvc, "/auth/register", params, ValidEnum.USERNAME_NOT_EMPTY.getMsg());
    }

    @Test
    void testRegisterBeValid() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "username");
        params.put("password", "password");
        when(userService.insert("username", "password"))
                .thenReturn(new User().setId(1).setUsername("username").setPassword("password"));
        testPost(mvc, "/auth/register", params, "注册成功");
        when(userService.insert("username", "password"))
                .thenThrow(DuplicateKeyException.class);
        testPost(mvc, "/auth/register", params, "用户已存在");
    }


}