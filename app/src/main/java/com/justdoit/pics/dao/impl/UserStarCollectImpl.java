package com.justdoit.pics.dao.impl;

import android.content.Context;

import com.android.volley.Response;
import com.justdoit.pics.dao.UserStarCollect;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.FormJsonObjRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljz on 2015/11/27.
 */
public class UserStarCollectImpl implements UserStarCollect {
    @Override
    public void Star(Context context,int pk, Response.Listener okListener, Response.ErrorListener errorListener) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("topic", pk + "");
        FormJsonObjRequest request = new FormJsonObjRequest(context, Constant.HOME_URL+Constant.USER_START, params, null,okListener, errorListener);
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void Collect(Context context,int pk, Response.Listener okListener, Response.ErrorListener errorListener) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("topic", pk + "");
        FormJsonObjRequest request = new FormJsonObjRequest(context, Constant.HOME_URL+Constant.USER_COLLECT, params, null,okListener, errorListener);
        NetSingleton.getInstance(context).addToRequestQueue(request);
    }
}
