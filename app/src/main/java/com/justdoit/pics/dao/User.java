package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;

import java.util.Map;

/**
 * 用户接口
 * 1.修改个人信息
 * Created by mengwen on 2015/11/22.
 */
public interface User {

    /**
     * 获取用户信息
     * @param context
     * @param userId
     * @param params
     * @param listener
     * @param errorListener
     */
    public abstract void getUserInfo(Context context, int userId, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener);

    /**
     * 修改用户信息
     * @param context
     * @param userId 用户id
     * @param params 修改的参数
     * @param fileParams 上传的文件(参数名和文件路径的map;文件名由PostFormRequest类生成)
     * @param listener  操作成功后的listener
     * @param errorListener  操作失败后的listener
     */
    public abstract void changeUserInfo(Context context, int userId, Map<String, String> params, Map<String, String> fileParams, Response.Listener listener, Response.ErrorListener errorListener);

    /**
     * 登录接口
     * @param context
     * @param params
     * @param listener
     * @param errorListener
     */
    public abstract void login(Context context, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener);

    /**
     * 注册接口
     * @param context
     * @param params
     * @param listener
     * @param errorListener
     */
    public abstract void regist(Context context, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener);
}
