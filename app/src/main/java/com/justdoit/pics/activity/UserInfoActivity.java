package com.justdoit.pics.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.UserInfoViewPagerAdapter;
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
    private TabLayout tabLayout;
    private CollapsingToolbarLayout toolbarLayout;

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

        initBriefView();

        initTabLayout();

    }

    /**
     * 设置toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.user_into_toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.user_info_toolbar_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示上一级按钮

        toolbarLayout.setTitleEnabled(false); // 设置title不跟随layout缩放
    }

    private void initBriefView() {
        TextView usernameTv = (TextView) findViewById(R.id.user_info_username);

        String username = App.getUserName();

        // 设置用户名
        if (username == null || TextUtils.isEmpty(username)) {
            usernameTv.setText(R.string.user_info);
        } else {
            usernameTv.setText(username);
        }

        // 设置居住地
        TextView locationTv = (TextView) findViewById(R.id.user_info_location);

    }

    /**
     * 设置tab layout和viewpager
     */
    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.user_info_tab_layout);

        ViewPager viewpager = (ViewPager) findViewById(R.id.user_info_view_pager);

        if (viewpager != null) {
            setupViewPager(viewpager);
        }

        tabLayout.setupWithViewPager(viewpager); // 装载viewpager
    }

    /**
     * 初始化viewpager
     * @param viewpager
     */
    private void setupViewPager(ViewPager viewpager) {
        UserInfoViewPagerAdapter adapter = new UserInfoViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Fragment(), "简介");
        adapter.addFragment(new Fragment(), "信息");
        adapter.addFragment(new Fragment(), "收藏");

        viewpager.setAdapter(adapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 点击up按钮事件处理
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
