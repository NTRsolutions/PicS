package com.justdoit.pics.model;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljz on 2015/11/28.
 */
public class PostDeteleRequest<JSONObject> extends Request<JSONObject> {



    Response.Listener mListener;

    public PostDeteleRequest(int pk,Response.Listener oklistener, Response.ErrorListener listener) {
        super(Method.POST, Constant.HOME_URL + Constant.TOPIC + pk, listener);
        mListener = oklistener;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String> params = new HashMap<>();
        params.put("csrfmiddlewaretoken", App.getToken());
        params.put("_method=", "DELETE");
        return params;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(new org.json.JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
