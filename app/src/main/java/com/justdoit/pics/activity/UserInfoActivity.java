package com.justdoit.pics.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.text.TextUtils;
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
 *
 * 需要传递用户名参数username和用户userid
 * 通过传递过来的id和用户的id对比，如果相同就设置isUserOwn = true;否则isUserOwn = false;
 * 如果isUserOwn = false，询问服务器是否已经关注了，更改相应的控件
 *
 * TODO 添加修改信息和收藏页面
 * Created by mengwen on 2015/10/28.
 */
public class UserInfoActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private final static String TAG = "UserInfoActivity";

    static final int CHANGE_AVATAR_REQ_CODE = 1; // 修改头像的请求code
    static final int CHANGE_BACKGROUND_REQ_CODE = 2; // 修改背景的请求code
    private int userId = -1;
    private String username;
    private boolean isUserOwn = true; // true:用户自己的个人页面; false:其他人的个人页面;默认是用户自己的页面

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout container;

    private ImageView avatarImageView; // 头像

    private TextView userNameTv; // 用户名
    private TextView followersTv; // 粉丝数
    private TextView scannersTv; // 被查看次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initData();

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
     * 获取启动该activity传递过来的数据
     */
    private void initData() {
        Bundle data = getIntent().getExtras();
        username = data.getString(Constant.USERNAME_NAME);
        userId = data.getInt(Constant.USER_ID_NAME);

        if (userId == App.getUserId()) {
            isUserOwn = true;
        } else {
            isUserOwn = false;
        }
    }

    /**
     * 初始化view
     */
    private void initView() {

        container = (SwipeRefreshLayout) findViewById(R.id.user_info_refresh_container);

        initToolbar();

        initBriefView();

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

        // 如果用户名为空
        // 默认是显示"用户信息"
        // 否则显示username
        if (!TextUtils.isEmpty(username)) {
            getSupportActionBar().setTitle(username);
        } else {
            getSupportActionBar().setTitle(R.string.user_info);
        }


        toolbarLayout.setTitleEnabled(false); // 设置title不跟随layout缩放
    }

    /**
     * 初始化toolbar layout下面的简单介绍的布局
     */
    private void initBriefView() {
        userNameTv = (TextView) findViewById(R.id.user_info_username);
        followersTv = (TextView) findViewById(R.id.user_info_followers);
        scannersTv = (TextView) findViewById(R.id.user_info_scanners);

        userNameTv.setText(username);
        userNameTv.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG); // 字体加粗
        // TODO 用户简介的数据更新
        followersTv.setText("1020个关注者");
        scannersTv.setText("6,324次被查看");
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
        if (isUserOwn) {
            // TODO 收藏页面
            adapter.addFragment(new Fragment(), "收藏");
        }

        viewpager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUserOwn) {
            getMenuInflater().inflate(R.menu.activity_user_info, menu);
        } else {
            // TODO 对应其他用户的menu

        }

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
                choosePicture(CHANGE_AVATAR_REQ_CODE);
                return true;
            case R.id.action_change_background_image:
                // 修改背景图片
                choosePicture(CHANGE_BACKGROUND_REQ_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择图片的获取方式:
     * 1.拍照
     * 2.你的图片
     * 3.你的相册
     * @param requestCode 请求的code，CHANGE_AVATAR_REQ_CODE = 1; // 修改头像的请求code
     *                              CHANGE_BACKGROUND_REQ_CODE = 2; // 修改背景的请求code
     */
    private void choosePicture(final int requestCode) {
        // TODO 选择方式
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_picture).setItems(R.array.choose_picture_way, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 拍照
                        if (SystemUtil.hasCamera(UserInfoActivity.this)) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, requestCode);
                        }
                        break;
                    case 1:
                        // 打开图库
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, requestCode);
                        break;
                    case 2:
                        // 打开相册
                        // TODO 从服务器获取所有分享的图片

                        break;
                    default:
                        Log.e(TAG, "AlertDialog onClick() int which:" + which + " error");
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        // 如果垂直偏移量为0
        // 设置swipe refresh layout 为可用
        container.setEnabled(verticalOffset == 0);
    }
}
