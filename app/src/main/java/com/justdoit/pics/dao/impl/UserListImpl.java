package com.justdoit.pics.dao.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.justdoit.pics.dao.UserList;
import com.justdoit.pics.model.NetSingleton;

/**
 * Created by ljz on 2015/11/27.
 */
public class UserListImpl implements UserList {
    @Override
    public void getList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }


//    @Override
//    public void getUserList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
//        NetSingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    @Override
//    public void getUserCollectList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
//        NetSingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    @Override
//    public void getUserRecentList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
//        NetSingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    @Override
//    public void getUserRelationList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
//        NetSingleton.getInstance(context).addToRequestQueue(request);
//    }
//
//    @Override
//    public void getUserBestList(Context context, String url, Response.Listener okListener, Response.ErrorListener errorListener) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,okListener,errorListener);
//        NetSingleton.getInstance(context).addToRequestQueue(request);
//    }
}
