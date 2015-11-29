package com.justdoit.pics.dao;

import android.content.Context;

import com.android.volley.Response;;

import java.util.Map;

/**
 * Created by ljz on 2015/11/28.
 */
public interface GetTopicCommentEdit {
    public abstract void GetTopic(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorlistener);

    public abstract void GetComment(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorlistener);

    public abstract void CreateComment(Context context, Map<String,String> params, Response.Listener oklistener, Response.ErrorListener errorListener);

    public abstract void DeteleTopic(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorListener);

}
