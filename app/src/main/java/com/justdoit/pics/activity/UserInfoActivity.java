package com.justdoit.pics.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.UserInfoViewPagerAdapter;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.bean.UserRelationListInfo;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.UserRelation;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.dao.impl.UserRelationImpl;
import com.justdoit.pics.fragment.BriefIntroFragment;
import com.justdoit.pics.fragment.MainFragment;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.util.ImageUtil;
import com.justdoit.pics.util.NetUtil;
import com.justdoit.pics.util.SystemUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息页面
 * <p/>
 * 需要传递用户名参数username和用户userid
 * 通过传递过来的id和用户的id对比，如果相同就设置isUserOwn = true;否则isUserOwn = false;
 * 如果isUserOwn = false，询问服务器是否已经关注了，更改相应的控件
 * <p/>
 * <p/>
 * 从数据库获取数据:getDataFromServer()
 * TODO 添加修改信息和收藏页面
 * Created by mengwen on 2015/10/28.
 */
public class UserInfoActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private final static String TAG = "UserInfoActivity";

    // 获取图片的方式
    // 0:拍照
    // 1:你的图库
    // 2:你的分享的所有图片

    private static enum REQUEST_CODE {
        takePhotos,
        usePhotos,
        useAlbum;

        static boolean isChangeAvatar; // 是否准备修改头像,false表示修改背景
    }

    private int userId = -1;
    private String username;
    private boolean isUserOwn = true; // true:用户自己的个人页面; false:其他人的个人页面;默认是用户自己的页面

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout container;

    private NetworkImageView avatarImageView; // 头像
    private ImageView backgroundImageView; // 背景图片

    private TextView userNameTv; // 用户名
    private TextView followersTv; // 粉丝数
    private TextView scannersTv; // 被查看次数

    private Button makeFriendsBtn; // follow和un-follow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initData();

        initView();

        initListener();

        getDataFromServer();
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
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            User user = new UserImpl();
            Map<String, String> fileParams = new HashMap<String, String>();
            Map<String, String> params = new HashMap<String, String>();
            params.put("_method", "PUT");  // 上传必须要这个参数

            // 用户修改头像
            if (REQUEST_CODE.isChangeAvatar) {
                switch (REQUEST_CODE.values()[requestCode]) {
                    case takePhotos:
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        String path = ImageUtil.saveBitmap(this, bitmap, 100);
                        avatarImageView.setImageBitmap(bitmap);

                        fileParams.put("avatar", path);
                        user.changeUserInfo(this, App.getUserId(),
                                params, fileParams,
                                okListener,
                                errorListener
                        );
                        break;
                    case usePhotos:
                        avatarImageView.setImageURI(data.getData());
                        // TODO 同步到服务器,并且处理本地UI

                        break;
                    case useAlbum:
                        // TODO 暂时没有添加分享图片做头像的功能
                        break;
                    default:
                        // 传递数据异常
                        Log.e(TAG, "requestCode error: requestCode = " + requestCode);
                        break;
                }
            } else {
                // 修改用户背景
                switch (REQUEST_CODE.values()[requestCode]) {
                    case takePhotos:
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        backgroundImageView.setImageBitmap(bitmap);
                        break;
                    case usePhotos:
                        backgroundImageView.setImageURI(data.getData());
                        break;
                    case useAlbum:
                        break;
                    default:
                        // 传递数据异常
                        Log.e(TAG, "requestCode error: requestCode = " + requestCode);
                        break;
                }
            }
        }
    }

    private Response.Listener okListener; // 成功监听器
    private Response.ErrorListener errorListener; // 失败监听器

    private Response.Listener<JsonObject> okFollowingListener; // following和follower
    private Response.Listener<JsonObject> okFollowerListener; // following和follower

    /**
     * 初始化网络请求监听
     */
    private void initListener() {
        okListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Type type = new TypeToken<UserInfo>() {

                }.getType();
                UserInfo userInfo = gson.fromJson(String.valueOf(response), type);

                // 更新UI
                updateUI(userInfo);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                // 停止刷新，如果存在刷新
                if (container.isRefreshing()) {
                    container.setRefreshing(false);
                }
            }
        };
        okFollowingListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Type type = new TypeToken<UserRelationListInfo>() {

                }.getType();
                UserRelationListInfo list = gson.fromJson(String.valueOf(response), type);
                ((BriefIntroFragment) viewPagerAdapter.getItem(0)).updateFollowings(list);

                if (!isUserOwn) {
                    updateFriendsBtn(list);
                }
            }
        };

        okFollowerListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Type type = new TypeToken<UserRelationListInfo>() {

                }.getType();
                UserRelationListInfo list = gson.fromJson(String.valueOf(response), type);
                ((BriefIntroFragment) viewPagerAdapter.getItem(0)).updateFollowers(list);
            }
        };
    }

    /**
     * 从服务器获取数据
     * 获取成功后，okListener
     * 使用updateUI()
     * 更新Activity和简介fragment的UI
     */
    private void getDataFromServer() {
        initListener();

        // 如果网络通畅
        if (NetUtil.isNetworkAvailable(this)) {
            User user = new UserImpl();
            user.getUserInfo(this, userId, null, okListener, errorListener);
            // 关系获取
            UserRelation userRelation = new UserRelationImpl();
            userRelation.getUserFollowingRelations(this, okFollowingListener, errorListener);
            userRelation.getUserFollowerRelations(this, okFollowerListener, errorListener);
        } else {
            // TODO 没有网络
            Toast.makeText(this, "当前没有网络+-+", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 0：关注
     * 1：拉黑
     */
    private View.OnClickListener cancelFollowing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("relation_user", String.valueOf(userId));
            params.put("relation", String.valueOf(0));
            UserRelation userRelation = new UserRelationImpl();
            userRelation.cancelUserFollowingRelations(v.getContext(), params,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            makeFriendsBtn.setText("立即关注");
                            makeFriendsBtn.setOnClickListener(createFollowing);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserInfoActivity.this, "取消关注失败", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    };

    private View.OnClickListener createFollowing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("relation_user", String.valueOf(userId));
            params.put("relation", String.valueOf(0));
            UserRelation userRelation = new UserRelationImpl();
            userRelation.createUserFollowingRelations(v.getContext(), params,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            makeFriendsBtn.setText("取消关注");
                            makeFriendsBtn.setOnClickListener(cancelFollowing);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserInfoActivity.this, "关注失败", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    };

    private void updateFriendsBtn(UserRelationListInfo listInfo) {
        for (int i = 0; i < listInfo.getCount(); i++) {
            if (userId == listInfo.getResults().get(i).getRelation_user().getId()) {
                makeFriendsBtn.setText("取消关注");
                makeFriendsBtn.setOnClickListener(cancelFollowing);
                // 跳出函数
                return;
            }
        }

        makeFriendsBtn.setText("立即关注");
        makeFriendsBtn.setOnClickListener(createFollowing);
    }

    /**
     * 更新Activity UI和简介fragment UI
     *
     * @param userInfo
     */
    private void updateUI(UserInfo userInfo) {
        // 图片加载
        ImageLoader imageLoader = NetSingleton.getInstance(this).getImageLoader();

        // toolbar
        getSupportActionBar().setTitle(userInfo.getUsername());


        // brief view
        followersTv.setText(userInfo.getFollowers_count() + "个关注者");
        if (userInfo.getAvatar() != null) {
            avatarImageView.setImageUrl(String.valueOf(userInfo.getAvatar()), imageLoader);
        }

        avatarImageView.setDefaultImageResId(R.mipmap.user_info_def_avatar);
        avatarImageView.setErrorImageResId(R.drawable.ic_broken_image_black_48dp);

        // brief fragment UI
        // 0: briefIntroFragment
        // 1: mainFragment
        // 2:
        ((BriefIntroFragment) viewPagerAdapter.getItem(0)).updateUserInfo(userInfo);

        // 停止刷新，如果正在刷新
        if (container.isRefreshing()) {
            container.setRefreshing(false);
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

        // 如果网络通畅
        if (NetUtil.isNetworkAvailable(this)) {
            getDataFromServer();
        } else {
            // TODO 没有网络
            Toast.makeText(this, "当前没有网络+-+", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化view
     */
    private void initView() {

        container = (SwipeRefreshLayout) findViewById(R.id.user_info_refresh_container);
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });

        initToolbar();

        initBriefView();

        initTabLayout();

    }

    /**
     * 设置toolbar
     */
    private void initToolbar() {
        avatarImageView = (NetworkImageView) findViewById(R.id.user_info_avatar_iv);
        backgroundImageView = (ImageView) findViewById(R.id.user_info_bg_iv);
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

    private UserInfoViewPagerAdapter viewPagerAdapter; // Viewpager adapter

    /**
     * 初始化viewpager
     *
     * @param viewpager
     */
    private void setupViewPager(ViewPager viewpager) {
        viewPagerAdapter = new UserInfoViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(BriefIntroFragment.newInstance(new UserInfo(), isUserOwn), "简介");
        viewPagerAdapter.addFragment(MainFragment.newInstance(MainFragment.USERINFO,username,userId), "信息");
        if (isUserOwn) {
            // TODO 收藏页面
            viewPagerAdapter.addFragment(new Fragment(), "收藏");
        }

        viewpager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUserOwn) {
            getMenuInflater().inflate(R.menu.activity_user_info, menu);
        } else {
            // TODO 没有测试
            getMenuInflater().inflate(R.menu.activity_user_info_other, menu);
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_change_avatar:
                // 打开修改用户头像页面
                // 首先判断是否有拍照功能
                choosePicture(true);
                return true;
            case R.id.action_change_background_image:
                // 修改背景图片
                choosePicture(false);
                return true;
            case R.id.action_logout:
                // 退出登录
                App.logout(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择图片的获取方式:
     * 0.拍照
     * 1.你的图库
     * 2.你的分享的所有图片
     *
     * @param isChangeAvatar true:修改头像
     *                       false:修改背景
     */
    private void choosePicture(boolean isChangeAvatar) {

        REQUEST_CODE.isChangeAvatar = isChangeAvatar;// 返回结果的时候,判断怎么处理返回的data

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_picture).setItems(R.array.choose_picture_way, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (REQUEST_CODE.values()[which]) {
                    case takePhotos:
                        // 拍照
                        if (SystemUtil.hasCamera(UserInfoActivity.this)) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CODE.takePhotos.ordinal());
                        }
                        break;
                    case usePhotos:
                        // 打开图库
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*"); // 所有格式的图片
                        intent.addCategory(Intent.CATEGORY_OPENABLE); // 可打开文件
                        startActivityForResult(intent, REQUEST_CODE.usePhotos.ordinal());
                        break;
                    case useAlbum:
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
