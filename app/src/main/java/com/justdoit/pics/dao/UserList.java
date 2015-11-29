package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;

/**
 * Created by ljz on 2015/11/27.
 */
public interface UserList {

    public abstract void getList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);

//    public abstract void getUserList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);
//
//    public abstract void getUserCollectList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);
//
//    public abstract void getUserRecentList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);
//
//    public abstract void getUserRelationList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);
//
//    public abstract void getUserBestList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);

//    public abstract void getUserSearchList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener);
}
