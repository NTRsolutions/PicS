package com.justdoit.pics.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.justdoit.pics.dao.UserRelation;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.FormRequest;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.FormJsonObjRequest;

import java.util.Map;

/**
 * Created by mengwen on 2015/11/26.
 */
public class UserRelationImpl implements UserRelation {
    @Override
    public void getUserFollowingRelations(Context context, Response.Listener okListener, Response.ErrorListener errorListener) {
        JsonRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Constant.HOME_URL + Constant.FOLLOWING_URL_SUFFIX,
                okListener, errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void getUserFollowerRelations(Context context, Response.Listener okListener, Response.ErrorListener errorListener) {
        JsonRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Constant.HOME_URL + Constant.FOLLOWER_URL_SUFFIX,
                okListener, errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void cancelUserRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        FormRequest request = new FormJsonObjRequest(
                context, Constant.HOME_URL + Constant.CANCEL_FOLLOWING_URL_SUFFIX,
                params, okListener, errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void createUserRelations(Context context, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        FormRequest request = new FormJsonObjRequest(
                context, Constant.HOME_URL + Constant.CREATE_FOLLOWING_URL_SUFFIX,
                params, okListener, errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }
}
