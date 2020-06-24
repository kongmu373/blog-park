package com.kongmu373.park.controller;

import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.entity.BlogVo;
import com.kongmu373.park.service.BlogService;
import com.kongmu373.park.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private BlogService blogService;

    private UserService userService;

    @Inject
    public BlogController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping("")
    public CommonResult<List<BlogVo>> getBlogs(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }

        return blogService.selectListByUserId(page, 10, userId);
    }


    @PostMapping("")
    public CommonResult<BlogVo> createBlog(@RequestBody Map<String, String> params) {
        return userService.getCurrentUser()
                .map(user->blogService.createBlog(user,params))
                .orElse(CommonResult.fail("登录后才能操作"));
    }


    @GetMapping("/{blogId}")
    public CommonResult<BlogVo> getBlogById(@PathVariable("blogId") Integer blogId) {
        return blogService.getBlogById(blogId);
    }

    @PatchMapping("/{blogId}")
    public CommonResult<BlogVo> updateBlog(@PathVariable("blogId") int blogId, @RequestBody Map<String, String> params) {
        return userService.getCurrentUser()
                .map(user->blogService.update(blogId,params, user))
                .orElse(CommonResult.fail("登录后才能操作"));

    }

    @DeleteMapping("/{blogId}")
    public CommonResult<BlogVo> deleteBlog(@PathVariable("blogId") int blogId) {
        return userService.getCurrentUser()
                .map(user->blogService.deleteById(blogId,user.getId()))
                .orElse(CommonResult.fail("登录后才能操作"));
    }
}
