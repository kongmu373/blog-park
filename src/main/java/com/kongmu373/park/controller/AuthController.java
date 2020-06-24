package com.kongmu373.park.controller;


import com.kongmu373.park.common.LoginResult;
import com.kongmu373.park.entity.User;
import com.kongmu373.park.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

import static com.kongmu373.park.utils.ValidUtils.validParamAndReturnResult;
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
    public LoginResult auth() {
        return userService.getCurrentUser()
                .map(user -> LoginResult.success(user, null, true))
                .orElse(LoginResult.fail(null, false));
    }


    @PostMapping("/register")
    public LoginResult register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        try {
            validParamAndReturnResult(validUserName(username));
            validParamAndReturnResult(validPassword(password));
        } catch (RuntimeException e) {
            return LoginResult.fail(e.getMessage(), false);
        }
        try {
            User user = userService.insert(username, password);
            return LoginResult.success(user, "注册成功", null);
        } catch (DuplicateKeyException e) {
            return LoginResult.success(null, "用户已存在", false);

        }
    }


    @PostMapping("/login")
    public LoginResult login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        try {
            User user = userService.selectByUserName(username);
            if (user == null) {
                return LoginResult.fail("用户不存在", null);
            }
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success(user, "登录成功", null);
        } catch (BadCredentialsException e) {
            return LoginResult.fail("密码不正确", null);
        }
    }

    @GetMapping("/logout")
    public LoginResult logout() {
        return userService.getCurrentUser()
                .map(user -> {
                    SecurityContextHolder.clearContext();
                    return LoginResult.success(null, "注销成功", null);
                })
                .orElse(LoginResult.fail("用户尚未登录", null));

    }
}
