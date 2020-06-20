package com.kongmu373.park.controller;


import com.kongmu373.park.common.CommonResult;
import com.kongmu373.park.common.ValidEnum;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

import static com.kongmu373.park.utils.ValidUtils.validPassword;
import static com.kongmu373.park.utils.ValidUtils.validUserName;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private UserService userService;

    private AuthenticationManager authenticationManager;


    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("")
    public CommonResult<User> auth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.selectByUserName(authentication.getName());
        if (user == null) {
            return CommonResult.success(null, null, false);
        }
        return CommonResult.success(user.setPassword(null).setDeleted(null), null, true);
    }

    @PostMapping("/register")
    public CommonResult<User> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        try {
            validParamAndReturnCommonResult(validUserName(username));
            validParamAndReturnCommonResult(validPassword(password));
            User user = userService.insert(username, password);
            return CommonResult.success(user, "注册成功", null);
        } catch (RuntimeException e) {
            return CommonResult.fail(e.getMessage(), false);
        }
    }

    private void validParamAndReturnCommonResult(ValidEnum paramValid) {
        if (paramValid != null) {
            throw new RuntimeException(paramValid.getMsg());
        }
    }

    @PostMapping("/login")
    public CommonResult<User> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return CommonResult.fail("用户不存在", null);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User user = userService.selectByUserName(username);
            return CommonResult.success(user.setPassword(null).setDeleted(null), "登录成功", null);
        } catch (BadCredentialsException e) {
            return CommonResult.fail("密码不正确", null);
        }
    }
}
