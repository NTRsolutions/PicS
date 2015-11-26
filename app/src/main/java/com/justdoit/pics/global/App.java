package com.justdoit.pics.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;
import android.util.Log;

import com.justdoit.pics.model.MyCookieStore;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 初始化userId和username
 * 获取token
 * Created by mengwen on 2015/10/27.
 */
public class App extends Application {

    public static CookieManager cookieManager;

    public static int USER_ID = -1; // 用户id，唯一标识，默认值为-1

    public static String USER_NAME = ""; // 用户名，默认为空字符串

    public static String token = "";

    private final static String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        setCookieManager(); // 设置cookie

        initUserData();

        initToken();

    }

    /**
     * 初始化用户信息
     */
    private void initUserData() {
        // 初始化userid,不存在就设为-1
        SharedPreferences sp = getSharedPreferences(Constant.USER_INFO_PREFS, MODE_PRIVATE);
        USER_ID = sp.getInt(Constant.USER_ID_NAME, -1);
        USER_NAME = sp.getString(Constant.USERNAME_NAME, "");
    }

    public void initToken() {

        SharedPreferences sp = getSharedPreferences(Constant.COOKIES_PREFS, MODE_PRIVATE);
        token = sp.getString(Constant.TOKEN_NAME, "");

        // TODO token仍然为空
    }

    public static String getToken() {

        if (TextUtils.isEmpty(token)) {
            for (HttpCookie c : cookieManager.getCookieStore().getCookies()) {
                if (c.getName().equals(Constant.TOKEN_NAME)) {
                    token = c.getValue();
                    Log.e(TAG, token);
                }
            }
        } else {
            Log.e(TAG, token);
        }

        return token;
    }


    /**
     * 初始化和设置cookie
     *
     * @return
     */
    public void setCookieManager() {

        // 初始化cookie,设置token名称
        if (cookieManager == null) {
            try {
                cookieManager = new CookieManager(new MyCookieStore(this, new URI(Constant.HOME_URL), Constant.TOKEN_NAME), CookiePolicy.ACCEPT_ALL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.e(TAG, "new URI() failed");
            }
            CookieHandler.setDefault(cookieManager);
        }
    }

    /**
     * 退出登录
     * 清理所有用户信息和cookies
     * 设置token为空字符串
     * @param context
     */
    public static void logout(Context context) {
        // 清除cookies
        cookieManager.getCookieStore().removeAll();
        // 清除preference
        clearAllPrefs(context);

        USER_ID = -1;

        token = "";
    }

    /**
     * 清除所有preferences
     * cookies
     * userinfo
     * @param context
     */
    public static void clearAllPrefs(Context context) {
        context.getSharedPreferences(Constant.COOKIES_PREFS, MODE_PRIVATE).edit()
                .clear()
                .commit();
        context.getSharedPreferences(Constant.USER_INFO_PREFS, MODE_PRIVATE).edit()
                .clear()
                .commit();
    }

    /**
     * 返回是否登录,通过判断USER_ID是否等于-1，-1就是没有登录
     * @return
     *   true:已登录
     *   false:没登录
     */
    public static boolean isLogin() {
        return USER_ID == -1 ? false : true;
    }

    public static void setUserId(int userId) {
        USER_ID = userId;
    }

    public static void setUserName(String userName) {
        USER_NAME = userName;
    }

    public static int getUserId() {
        return USER_ID;
    }

    public static String getUserName() {
        return USER_NAME;
    }
}
