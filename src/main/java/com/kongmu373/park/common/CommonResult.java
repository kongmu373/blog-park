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

    protected CommonResult() {

    }

    protected CommonResult(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResult<T> success(String msg, T data) {
        return new CommonResult<>(SUCCESS, msg, data);
    }

    public static <T> CommonResult<T> fail(String msg) {
        return new CommonResult<>(FAIL, msg, null);
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
