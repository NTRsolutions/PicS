package com.justdoit.pics.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.UserInfoViewPagerAdapter;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.fragment.BriefIntroFragment;
import com.justdoit.pics.fragment.MainFragment;
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
public class UserInfoActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private final static String TAG = "UserInfoActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        appBarLayout.addOnOffsetChangedListener(this); // 添加监听appbar layout是否可滚动的listener
    }

    @Override
    protected void onPause() {
        super.onPause();

        appBarLayout.removeOnOffsetChangedListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {

        container = (SwipeRefreshLayout) findViewById(R.id.user_info_refresh_container);

        initToolbar();

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

    /**
     * 设置tab layout和viewpager
     */
    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.user_info_tab_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.user_info_appbar);

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

        adapter.addFragment(BriefIntroFragment.newInstance(), "简介");
        adapter.addFragment(MainFragment.newInstance(MainFragment.NO_FOOTERANDHEADER), "信息");
        adapter.addFragment(new Fragment(), "收藏");
        viewpager.setAdapter(adapter);

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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        // 如果垂直偏移量为0
        // 设置swipe refresh layout 为可用
        container.setEnabled(verticalOffset == 0);
    }
}
