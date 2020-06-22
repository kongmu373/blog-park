package com.kongmu373.park.utils;

import com.kongmu373.park.common.ValidEnum;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public final class ValidUtils {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z_\\u4e00-\\u9fa5]{1,}$");

    private ValidUtils() {

    }

    public static ValidEnum validUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            return ValidEnum.USERNAME_NOT_EMPTY;
        }
        if (username.length() < 1 || username.length() > 15) {
            return ValidEnum.USERNAME_LENGTH;
        }
        if (!PATTERN.matcher(username).find()) {
            return ValidEnum.REQUIRE_CHAR;
        }
        return null;
    }

    public static ValidEnum validPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return ValidEnum.PASSWORD_NOT_EMPTY;
        }
        if (password.length() < 6 || password.length() > 16) {
            return ValidEnum.PASSWORD_LENGTH;
        }
        return null;
    }


    public static Object validParamAndReturnCommonResult(ValidEnum paramValid) throws RuntimeException {
        if (paramValid != null) {
            throw new RuntimeException(paramValid.getMsg());
        }
        return null;
    }
}
