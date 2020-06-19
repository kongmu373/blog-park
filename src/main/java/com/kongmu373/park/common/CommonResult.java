package com.kongmu373.park.common;

import java.io.Serializable;

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
    private String message;

    /**
     * 返回信息
     */
    private T data;

    public static <T> CommonResult<T> success(T data, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.status = SUCCESS;
        result.data = data;
        result.message = message;
        return result;
    }

    public static <T> CommonResult<T> fail(String message) {
        CommonResult<T> result = new CommonResult<>();
        result.status = FAIL;
        result.message = message;
        return result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
