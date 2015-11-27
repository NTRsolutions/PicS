package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;

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
}
