package com.kongmu373.park.common;

public enum ValidEnum {
    USERNAME_NOT_EMPTY("用户名不能为空"),
    PASSWORD_NOT_EMPTY("密码不能为空"),
    USERNAME_LENGTH("长度1到15个字符"),
    REQUIRE_CHAR("只能是字母数字下划线中文"),
    PASSWORD_LENGTH("长度6到16个任意字符"),
    TITLE_VALID("博客标题不能为空，且不超过100个字符"),
    CONTENT_VALID("博客内容不能为空，且不超过10000个字符");


    private String msg;

    ValidEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
