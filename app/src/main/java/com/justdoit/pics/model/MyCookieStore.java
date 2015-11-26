package com.justdoit.pics.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * 存储和初始化加载cookie的类
 * 在add（）方法自动存储该uri的cookies的值到preference
 * 包括token值
 * TODO 存储cookies的相关属性
 * Created by mengwen on 2015/10/27.
 */
public class MyCookieStore implements CookieStore {

    private final String TAG = "MyCookieStore";

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String SESSION_COOKIE = "sessionid";

    private CookieStore cookieStore;
    private String token = "";
    private String tokenName = "test"; // token名称，默认为test
    private SharedPreferences sp;

    private URI uri;

    public MyCookieStore(Context context, URI uri) {
        this(context, uri, null);
    }

    public MyCookieStore(Context context, URI uri, String tokenName) {
        this.uri = uri;

        // 设置token值
        if (tokenName != null && !tokenName.isEmpty()) {
            this.tokenName = tokenName;
        }

        cookieStore = new CookieManager().getCookieStore();

        sp = context.getSharedPreferences(Constant.COOKIES_PREFS, Context.MODE_PRIVATE);

        if (sp.getAll().isEmpty()) {
            // 网络请求cookies
            initCookiesFromReq(context, uri);
        } else {
            // 加载保存cookie
            for (Map.Entry<String, ?> entry : sp.getAll().entrySet()) {
                HttpCookie cookie = new HttpCookie(entry.getKey(), String.valueOf(entry.getValue()));
                cookie.setVersion(0);
                cookie.setPath("/");
                cookie.setDomain(uri.getHost());
                cookieStore.add(uri, cookie);
            }
        }
    }


    @Override
    public void add(URI uri, HttpCookie cookie) {
        cookieStore.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return cookieStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return cookieStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return cookieStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return cookieStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return cookieStore.removeAll();
    }

    public String getTokenName() {
        return tokenName;
    }

    /**
     * 初始化
     * 获取网络cookies
     * @param context
     * @param uri
     */
    public void initCookiesFromReq(final Context context, final URI uri) {
        // 请求服务器生成cookie

        final StringRequest request = new StringRequest(
                Request.Method.HEAD, uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "获取网络cookies成功");

                        SharedPreferences.Editor editor = sp.edit();
                        for (HttpCookie cookie : App.cookieManager.getCookieStore().getCookies()) {
                            editor.putString(cookie.getName(), cookie.getValue());
                            Log.e(TAG, "name:" + cookie.getName());
                            Log.e(TAG, "value:" + cookie.getValue());
                        }

                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "获取网络cookies失败");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;

                Log.e(TAG, "hahah");

                if (headers.containsKey(SET_COOKIE_KEY)
                        && headers.get(SET_COOKIE_KEY).contains(SESSION_COOKIE)) {
                    Log.e(TAG, headers.get(SET_COOKIE_KEY).toString());
                }

                return super.parseNetworkResponse(response);
            }
        };
        NetSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}
