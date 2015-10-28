package com.justdoit.pics.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 用户信息页面
 * TODO 添加修改信息和收藏页面
 * Created by mengwen on 2015/10/28.
 */
public class UserInfoActivity extends AppCompatActivity {

    private final static String TAG = "UserInfoActivity";

    private UserInfo userInfo = null;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getDataFromServer();

        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {

        initToolbar();

    }

    /**
     * 设置toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.user_into_toolbar);
        setSupportActionBar(toolbar);

        if (userInfo == null) {
            toolbar.setTitle(R.string.user_info);
        } else {

        }
    }

    /**
     * 获取服务器用户数据
     */
    public void getDataFromServer() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.HOME_URL + Constant.USER_INFO_URL_SUFFIX + App.getUserId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<UserInfo>(){}.getType();
                        userInfo = gson.fromJson(String.valueOf(response), type);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        NetSingleton.getInstance(this).addToRequestQueue(request);

    }

}
