package com.justdoit.pics.dao.impl;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.justdoit.pics.dao.GetTopicCommentEdit;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostDeteleRequest;
import com.justdoit.pics.model.FormJsonObjRequest;

import java.util.Map;

/**
 * Created by ljz on 2015/11/28.
 */
public class GetTopicCommentEditImpl implements GetTopicCommentEdit {
    @Override
    public void GetTopic(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorlistener) {
        JsonObjectRequest mrequest = new JsonObjectRequest(Constant.HOME_URL + Constant.TOPIC +pk, oklistener, errorlistener);
        NetSingleton.getInstance(context).addToRequestQueue(mrequest);
    }

    @Override
    public void GetComment(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorlistener) {
        JsonObjectRequest mrequest = new JsonObjectRequest(Constant.HOME_URL + Constant.COMMENT_LIST +"?id="+pk, oklistener, errorlistener);
        NetSingleton.getInstance(context).addToRequestQueue(mrequest);
    }

    @Override
    public void CreateComment(Context context, Map<String, String> params, Response.Listener oklistener, Response.ErrorListener errorListener) {
        FormJsonObjRequest mrequest = new FormJsonObjRequest(context,Constant.HOME_URL + Constant.USER_COMMENT,params, oklistener, errorListener);
        NetSingleton.getInstance(context).addToRequestQueue(mrequest);
    }

    @Override
    public void DeteleTopic(Context context, int pk, Response.Listener oklistener, Response.ErrorListener errorListener) {
        PostDeteleRequest mrequest = new PostDeteleRequest(pk,oklistener,errorListener);
        NetSingleton.getInstance(context).addToRequestQueue(mrequest);
    }
}
