package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;

import java.util.Map;

/**
 * Created by mengwen on 2015/11/26.
 */
public interface UserRelation {

    /**
     * 获取当前用户的关注列表
     * @param context
     * @param okListener
     * @param errorListener
     */
    public abstract void getUserFollowingRelations(Context context, Response.Listener okListener, Response.ErrorListener errorListener);

    public abstract void getUserFollowerRelations(Context context, Response.Listener okListener, Response.ErrorListener errorListener);

    /**
     * 取消关系
     * params
     * 0：关注
     * 1：拉黑
     * @param context
     * @param okListener
     * @param errorListener
     */
    public abstract void cancelUserRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener);

    /**
     * 创建关系
     * params
     * 0：关注
     * 1：拉黑
     * @param context
     * @param okListener
     * @param errorListener
     */
    public abstract void createUserRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener);

}
