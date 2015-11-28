package com.justdoit.pics.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.fragment.MainFragment;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 登录或者游客(未登录的)之后跳转的目标activity,需要登录的userid
 *
 *
 * TODO:整理
 * Created by mengwen on 2015/10/26.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;

    private String username;
    private int userid;

    public UserInfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!App.isLogin()) { // 未登录显示默认用户头像
            toolbar.setNavigationIcon(R.mipmap.user_info_def_avatar);
            getSupportActionBar().setTitle(R.string.visitor);
        } else { // 登录了就显示用户头像 TODO:用户头像获取
            username = App.getUserName();
            userid = App.getUserId();
            getUserInfoFromServer();
            getSupportActionBar().setTitle(username);
        }
    }

    private void initView() {



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (App.isLogin()) {
            username = App.getUserName();
            userid = App.getUserId();
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.isLogin()) {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.USER_ID_NAME, App.getUserId());
                    bundle.putString(Constant.USERNAME_NAME, App.getUserName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // 带上要跳转到UserInfo的标识
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.ACTION_KEY, "UserInfoActivity");
                    startActivity(intent);
                }

            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, MainFragment.newInstance(MainFragment.RECENT,username,userid),"MainFragment")
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void getUserInfoFromServer(){
        JsonObjectRequest mrequest = new JsonObjectRequest(Constant.HOME_URL + Constant.USER_INFO_URL_SUFFIX + userid, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson mgson = new Gson();
                    userinfo = mgson.fromJson(String.valueOf(response),UserInfo.class);
                }finally {
                    getUserAvatarFromServer();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("test",error.toString());
                getUserInfoFromServer();
            }
        });

        RequestQueue mRequest = NetSingleton.getInstance(this).getRequestQueue();
        mRequest.add(mrequest);
    }

    private void getUserAvatarFromServer(){
        ImageRequest imageRequest = new ImageRequest(
                userinfo.getAvatar(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Drawable drawable =new BitmapDrawable(null,response);
                        toolbar.setNavigationIcon(drawable);
                    }
                }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getUserAvatarFromServer();
                Log.e("test",error.toString());
            }
        });
        RequestQueue mRequest = NetSingleton.getInstance(this).getRequestQueue();
        mRequest.add(imageRequest);
    }
}
