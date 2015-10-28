package com.justdoit.pics.model;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 表单提交返回json object
 * Created by mengwen on 2015/10/28.
 */
public class PostFormJsonObjRequest<JSONObject> extends PostFormRequest<JSONObject> {
    public PostFormJsonObjRequest(String url, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(url, params, okListener, errorListener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new org.json.JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
