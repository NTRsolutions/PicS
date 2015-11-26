package com.justdoit.pics.model;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ljz on 2015/11/25.
 */
public class PostFormJsonArrayRequest<JSONArray> extends PostFormRequest<JSONArray> {
    public PostFormJsonArrayRequest(String url, Map params, Map fileParams, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(url, params, fileParams, okListener, errorListener);
    }

    public PostFormJsonArrayRequest(String url, Map params, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(url, params, okListener, errorListener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new org.json.JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
