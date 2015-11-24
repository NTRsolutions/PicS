package com.justdoit.pics.util;

import java.util.regex.Pattern;

/**
 * 验证工具类，多用在用户登录和注册
 * Created by mengwen on 2015/11/24.
 */
public class CheckUtil {

    public static boolean isUsernameValid(String username) {
        // 用户名必须6-30位,只能包含数字字母下划线,并以字母开头
        final String USER_NAME_MATCH_STR = "^[a-zA-Z][\\w]{5,29}$";
        Pattern pattern = Pattern.compile(USER_NAME_MATCH_STR);

        return pattern.matcher(username).matches();

    }

    public static boolean isPasswordValid(String password) {
        // TODO 需要商量一下
        return password.length() >= 6;
    }


    public static boolean isEmailValid(String email) {
        // 检查邮件字符串合法性
        final String EMAIL_MATCH_STR = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(EMAIL_MATCH_STR);
        return pattern.matcher(email).matches();
    }

}
