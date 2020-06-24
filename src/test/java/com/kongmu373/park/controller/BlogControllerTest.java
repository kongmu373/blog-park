package com.kongmu373.park.controller;


import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.entity.BlogVo;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.service.BlogService;
import com.kongmu373.park.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.kongmu373.park.utils.TestUtils.testDelete;
import static com.kongmu373.park.utils.TestUtils.testPatch;
import static com.kongmu373.park.utils.TestUtils.testPost;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class BlogControllerTest {

    private MockMvc blogMvc;

    private MockMvc authMvc;

    @Mock
    private UserService userService;

    @Mock
    private BlogService blogService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        blogMvc = MockMvcBuilders.standaloneSetup(new BlogController(blogService, userService)).build();

        authMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    @Test
    void testCreateBlog() throws Exception {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "user");
        loginParams.put("password", "pwd");
        User user = new User().setId(1).setUsername("user").setPassword("pwd");
        when(userService.selectByUserName("user")).thenReturn(user);
        testPost(authMvc, "/auth/login", loginParams, "\"msg\":\"登录成功\"");
        Map<String, String> blogParams = new HashMap<>();
        blogParams.put("title", "博客标题");
        blogParams.put("content", "博客内容");
        blogParams.put("description", "博客描述");
        BlogVo blogVo = new BlogVo().setTitle("博客标题")
                .setContent("博客内容")
                .setDescription("博客描述");
        when(userService.getCurrentUser()).thenReturn(Optional.ofNullable(user));
        when(blogService.createBlog(user, blogParams)).thenReturn(
                CommonResult.success("创建成功", blogVo.setUser(user.setPassword(null))));

        testPost(blogMvc, "/blog", blogParams, "创建成功");
    }

    @Test
    void testUpdateBlog() throws Exception {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "user");
        loginParams.put("password", "pwd");
        User user = new User().setId(1).setUsername("user").setPassword("pwd");
        when(userService.selectByUserName("user")).thenReturn(user);
        testPost(authMvc, "/auth/login", loginParams, "\"msg\":\"登录成功\"");
        Map<String, String> blogParams = new HashMap<>();
        blogParams.put("title", "update博客标题");
        blogParams.put("content", "update博客内容");
        blogParams.put("description", "update博客描述");
        BlogVo blogVo = new BlogVo()
                .setTitle("update博客标题")
                .setId(1)
                .setContent("update博客内容")
                .setDescription("update博客描述");
        when(userService.getCurrentUser()).thenReturn(Optional.ofNullable(user));
        when(blogService.update(1, blogParams, user)).thenReturn(
                CommonResult.success("修改成功", blogVo.setUser(user.setPassword(null))));
        testPatch(blogMvc, "/blog/1", blogParams, "修改成功");
    }

    @Test
    void testDeleteBlog() throws Exception {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("username", "user");
        loginParams.put("password", "pwd");
        User user = new User().setId(1).setUsername("user").setPassword("pwd");
        when(userService.getCurrentUser()).thenReturn(Optional.ofNullable(user));

        when(userService.getCurrentUser()).thenReturn(Optional.ofNullable(user));
        when(blogService.deleteById(1, user.getId())).thenReturn(
                CommonResult.success("删除成功", null));
        testDelete(blogMvc, "/blog/1", "删除成功");
    }

}
