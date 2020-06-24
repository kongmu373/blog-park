package com.kongmu373.park.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kongmu373.park.entity.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResult extends CommonResult<User> {
    /**
     * 是否登录
     */
    private Boolean isLogin;

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public static LoginResult success(User data, String message, Boolean isLogin) {
        return new LoginResult(SUCCESS, data, message, isLogin);
    }

    public static LoginResult fail(String message, Boolean isLogin) {
        return new LoginResult(FAIL, null, message, isLogin);
    }

    protected LoginResult(String status, User user, String msg, Boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }
}
