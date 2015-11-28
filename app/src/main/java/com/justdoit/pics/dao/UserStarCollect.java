package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;

import java.util.Map;

/**
 * Created by ljz on 2015/11/27.
 */
public interface UserStarCollect {
    public abstract void Star(Context context, int pk, Response.Listener okListener, Response.ErrorListener errorListener);

    public abstract void Collect(Context context, int pk, Response.Listener okListener, Response.ErrorListener errorListener);
}
