package com.kongmu373.park.controller;

import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public CommonResult<User> selectById(@RequestParam("id") Integer id) {
        return CommonResult.success(userService.selectById(id), "查找成功", true);
    }

}
