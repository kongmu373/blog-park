package com.kongmu373.park.service;

import com.kongmu373.park.common.BlogPageResult;
import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.entity.Blog;
import com.kongmu373.park.entity.BlogVo;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.mapper.BlogMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogVoServiceTest {

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogService blogService;


    @Test
    public void testSelectListByUserId() {
        List<BlogVo> blogVos = Arrays.asList(mock(BlogVo.class), mock(BlogVo.class));
        when(blogMapper.selectListByUserId(null, 2, 2)).thenReturn(blogVos);
        when(blogMapper.selectCountByUserId(null)).thenReturn(4);

        BlogPageResult result = (BlogPageResult) blogService.selectListByUserId(2, 2, null);
        verify(blogMapper).selectListByUserId(null, 2, 2);
        verify(blogMapper).selectCountByUserId(null);

        Assertions.assertEquals(2, result.getPage());
        Assertions.assertEquals(4, result.getTotal());
        Assertions.assertEquals(2, result.getTotalPage());
        Assertions.assertEquals("ok", result.getStatus());
    }


    @Test
    public void returnFailureWhenExceptionThrown() {
        when(blogMapper.selectListByUserId(any(), anyInt(), anyInt())).thenThrow(new RuntimeException());

        CommonResult<List<BlogVo>> result = blogService.selectListByUserId(1, 10, null);

        Assertions.assertEquals("fail", result.getStatus());
    }

    @Test
    public void testGetBlogById() {
        when(blogMapper.selectByIdWithUser(1)).thenReturn(mock(BlogVo.class));
        CommonResult<BlogVo> blogResult = blogService.getBlogById(1);
        verify(blogMapper).selectByIdWithUser(1);
        Assertions.assertEquals("ok", blogResult.getStatus());
        Assertions.assertEquals("获取成功", blogResult.getMsg());
    }

    @Test
    public void testCreateBlog() {
        User user = new User().setId(100);
        Blog blog = new Blog()
                .setUserId(100)
                .setContent("博客内容")
                .setDescription("博客描述")
                .setTitle("博客标题");
        Map<String, String> blogParams = new HashMap<>();
        blogParams.put("title", "博客标题");
        blogParams.put("content", "博客内容");
        blogParams.put("description", "博客描述");
        blogService.createBlog(user, blogParams);
        verify(blogMapper).insert(blog);
    }
}
