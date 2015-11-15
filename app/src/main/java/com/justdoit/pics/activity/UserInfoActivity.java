package com.justdoit.pics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.justdoit.pics.fragment.BriefIntroFragment;
import com.justdoit.pics.fragment.MainFragment;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.util.SystemUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 用户信息页面
 * TODO 添加修改信息和收藏页面
 * Created by mengwen on 2015/10/28.
 */
public class UserInfoActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private final static String TAG = "UserInfoActivity";

    static final int CHANGE_AVATAR_REQ_CODE = 1; // 修改头像的请求code
    static final int CHANGE_BACKGROUND_REQ_CODE = 2; // 修改背景的请求code

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout container;

    private ImageView avatarImageView; // 头像

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHANGE_AVATAR_REQ_CODE:
                    // TODO 选择头像的上传和刷新工作
                    break;
                case CHANGE_BACKGROUND_REQ_CODE:
                    // TODO 选择背景图片刷新工作，不需要上传
                    break;
                default:
                    // 传递数据异常
                    Log.e(TAG, "requestCode error: requestCode = " + requestCode);
                    break;
            }
        }
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
        avatarImageView = (ImageView) findViewById(R.id.user_info_avatar_iv);
        toolbar = (Toolbar) findViewById(R.id.user_into_toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.user_info_toolbar_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示上一级按钮

        // 如果登录了，必须要有用户名
        // 用户名添加到text view
        // 然后添加到toolbar
        // 默认是显示"用户信息"
        if (App.isLogin()) {
            getSupportActionBar().setTitle(App.getUserName());
        } else {
            getSupportActionBar().setTitle(R.string.user_info);
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_info, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击up按钮事件处理
                finish();
                return true;
            case R.id.action_settings:
                // 打开设置页面
                return true;
            case R.id.action_change_avatar:
                // 打开修改用户头像页面
                // 首先判断是否有拍照功能
                // TODO 选择图片方式
                if (SystemUtil.hasCamera(this)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CHANGE_AVATAR_REQ_CODE);
                }
                return true;
            case R.id.action_change_background_image:
                // 修改背景图片
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CHANGE_BACKGROUND_REQ_CODE);
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
