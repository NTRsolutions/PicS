package com.justdoit.pics.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.fragment.MainFragment;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 登录或者游客(未登录的)之后跳转的目标activity,需要登录的userid
 *
 *
 * TODO:等待数据接口
 * Created by mengwen on 2015/10/26.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private App mApp;

    private String username;
    private int userid;
    private FrameLayout container;
    private ArrayList<Content> contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contents = new ArrayList<Content>();
        initView();
        getDataFormServer();
    }



    private void initView() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mApp = (App)getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.container);



        if (!App.isLogin()) { // 未登录显示默认用户头像
            toolbar.setNavigationIcon(R.mipmap.user_info_def_avatar);
            toolbar.setTitle(R.string.visitor);
        } else { // 登录了就显示用户头像 TODO:用户头像获取
            username = mApp.getUserName();
            userid = mApp.getUserId();
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
            toolbar.setTitle(username);
            Log.e("test",username);
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
                    Bundle b = new Bundle();
                    intent.putExtra(Constant.ACTION_KEY, "UserInfoActivity");
                    startActivity(intent);
                }

            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, MainFragment.newInstance(MainFragment.NORMAL),"MainFragment")
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /**
     * 显示dialog和隐藏login form
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        final RelativeLayout mProgressLayoutView = (RelativeLayout)findViewById(R.id.progress_layout);
        final ProgressBar mProgressView = (ProgressBar)findViewById(R.id.progressbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            container.setVisibility(show ? View.GONE : View.VISIBLE);
            container.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    container.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
            container.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void getDataFormServer(){
        showProgress(true);
        JsonArrayRequest mjson = new JsonArrayRequest("http://demo.gzqichang.com:8001/api/topic/list/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                showProgress(false);
                try {
//                    Log.e("test", "successful: " + response.get(0).toString());
                    Gson mgson = new Gson();
                    Content content = mgson.fromJson(String.valueOf(response.get(0)),Content.class);
                    Log.e("test","content: " + content.getTitle());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Log.e("test","error: " + error.toString());
            }
        });
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(mjson);
    }

}
