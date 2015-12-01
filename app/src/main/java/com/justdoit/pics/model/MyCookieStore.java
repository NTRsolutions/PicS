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
 * 存储和初始化从preference读取和加载cookie的值
 *
 * TODO 存储cookies的相关属性
 * Created by mengwen on 2015/10/27.
 */
public class MyCookieStore implements CookieStore {

    private final String TAG = "MyCookieStore";

    private CookieStore cookieStore;
    private String tokenName = "test"; // token名称，默认为test
    private SharedPreferences sp;


    public MyCookieStore(Context context, URI uri) {
        this(context, uri, null);
    }

    public MyCookieStore(Context context, URI uri, String tokenName) {

        // 设置token值
        if (tokenName != null && !tokenName.isEmpty()) {
            this.tokenName = tokenName;
        }

        cookieStore = new CookieManager().getCookieStore();

        sp = context.getSharedPreferences(Constant.COOKIES_PREFS, Context.MODE_PRIVATE);
        if (!sp.getAll().isEmpty()) {
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
}
