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
     * 取消关注
     * @param context
     * @param okListener
     * @param errorListener
     */
    public abstract void cancelUserFollowingRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener);

    /**
     * 关注
     * @param context
     * @param okListener
     * @param errorListener
     */
    public abstract void createUserFollowingRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener);

}
