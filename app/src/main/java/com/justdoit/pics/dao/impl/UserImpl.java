package com.justdoit.pics.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostFormJsonArrayRequest;
import com.justdoit.pics.model.PostFormJsonObjRequest;
import com.justdoit.pics.model.PostFormRequest;

import java.util.Map;

/**
 * 用户api接口
 * Created by mengwen on 2015/11/22.
 */
public class UserImpl implements User {

    @Override
    public void getUserInfo(Context context, int userId, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Constant.HOME_URL + Constant.USER_INFO_URL_SUFFIX + userId,
                listener, errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void changeUserInfo(Context context, int userId, Map<String, String> params, Map<String, String> fileParams, Response.Listener listener, Response.ErrorListener errorListener) {
        PostFormRequest request = new PostFormJsonObjRequest(
                Constant.HOME_URL + Constant.USER_INFO_URL_SUFFIX + userId + "/",
                params, fileParams, listener, errorListener
        );

        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void login(Context context, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        PostFormRequest request = new PostFormJsonObjRequest(
                Constant.HOME_URL + Constant.LOGIN_URL_SUFFIX,
                params, listener, errorListener
        );

        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void regist(Context context, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        PostFormRequest request = new PostFormJsonObjRequest(
                Constant.HOME_URL + Constant.REGIST_URL_SUFFIX,
                params, listener, errorListener
        );

        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void userTopic(Context context, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        PostFormJsonArrayRequest request = new PostFormJsonArrayRequest(
                Constant.HOME_URL + Constant.USER_TOPIC_LIST,
                params,listener,errorListener
        );
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }


}
