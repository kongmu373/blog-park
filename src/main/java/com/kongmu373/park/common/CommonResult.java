package com.kongmu373.park.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> implements Serializable {

    public static final String SUCCESS = "ok";
    public static final String FAIL = "fail";

    /**
     * 状态码
     */
    private String status;

    /**
     * 描述信息
     */
    private String msg;

    /**
     * 返回信息
     */
    private T data;

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

    public static <T> CommonResult<T> success(T data, String message, Boolean isLogin) {
        CommonResult<T> result = new CommonResult<>();
        result.status = SUCCESS;
        result.data = data;
        result.msg = message;
        result.isLogin = isLogin;
        return result;
    }

    public static <T> CommonResult<T> fail(String message, Boolean isLogin) {
        CommonResult<T> result = new CommonResult<>();
        result.status = FAIL;
        result.msg = message;
        result.isLogin = isLogin;
        return result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
