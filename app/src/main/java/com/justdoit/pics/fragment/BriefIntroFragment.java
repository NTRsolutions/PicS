package com.justdoit.pics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.BriefIntroAdapter;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.bean.UserRelationListInfo;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.UserRelation;
import com.justdoit.pics.dao.impl.UserRelationImpl;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.widget.CustomItemDecoration;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 简介fragment
 *
 * Created by mengwen on 2015/11/10.
 */
public class BriefIntroFragment extends Fragment {

    private static final String USER_INFO = "UserInfo";
    private static final String IS_USER_OWN = "IsUserOwn";
    private static final String TAG = "BriefIntroFragment";

    private RecyclerView briefIntroList;

    private BriefIntroAdapter briefIntroAdapter;

    /**
     * 创建简介fragment
     * 默认是自己的简介而不是其他人的
     * @return 简介fragment
     */
    public static BriefIntroFragment newInstance() {

        return new BriefIntroFragment();
    }

    /**
     * 传递用户信息
     *
     * @param userInfo
     * @param isUserOwn true:用户自己 false:其他用户
     * @return
     */
    public static BriefIntroFragment newInstance(UserInfo userInfo, boolean isUserOwn) {

        Bundle args = new Bundle();
        args.putSerializable(USER_INFO, userInfo);
        args.putBoolean(IS_USER_OWN, isUserOwn);

        BriefIntroFragment fragment = new BriefIntroFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        UserInfo userInfo;
        UserRelationListInfo followingList = null;
        UserRelationListInfo followerList = null;
        boolean isUserOwn = true;

        if (bundle != null) {
            userInfo = (UserInfo) bundle.getSerializable(USER_INFO);
            isUserOwn = bundle.getBoolean(IS_USER_OWN);
        } else {
            userInfo = new UserInfo();
        }

        if (isUserOwn) {
            // TODO 获取用户关系列表
            getDataFromServe();
        } else {

        }

        View view = inflater.inflate(R.layout.fragment_brief_intro, container, false);

        initView(view, isUserOwn); // 初始化view

        updateUserInfo(userInfo); // 更新UI

        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     * @param isUserOwn true:用户自己 false:其他用户
     */
    private void initView(View view, boolean isUserOwn) {

        briefIntroList = (RecyclerView) view.findViewById(R.id.brief_intro_list);

        briefIntroList.setLayoutManager(new LinearLayoutManager(view.getContext())); // 设置线性布局

        briefIntroAdapter = new BriefIntroAdapter(view.getContext(), isUserOwn);

        briefIntroList.setAdapter(briefIntroAdapter); // 设置adapter
    }

    /**
     *
     * 更新用户数据和UI
     * @param userInfo
     */
    public void updateUserInfo(UserInfo userInfo) {
        briefIntroAdapter.setUserInfo(userInfo);
        briefIntroAdapter.notifyDataSetChanged();
    }

    public void updateFollowings(UserRelationListInfo followings) {
        briefIntroAdapter.setFollowings(followings);
        briefIntroAdapter.notifyDataSetChanged();
    }

    public void updateFollowers(UserRelationListInfo followers) {
        briefIntroAdapter.setFollowers(followers);
        briefIntroAdapter.notifyDataSetChanged();
    }

    private Response.Listener<JsonObject> okFollowingListener; // following和follower
    private Response.Listener<JsonObject> okFollowerListener; // following和follower
    private Response.ErrorListener errorFollowListener; // following和follower

    public void initListener() {
        okFollowingListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Type type = new TypeToken<UserRelationListInfo>() {

                }.getType();
                UserRelationListInfo list = gson.fromJson(String.valueOf(response), type);
                updateFollowings(list);
            }
        };

        okFollowerListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Type type = new TypeToken<UserRelationListInfo>() {

                }.getType();
                UserRelationListInfo list = gson.fromJson(String.valueOf(response), type);
                updateFollowers(list);
            }
        };


        errorFollowListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
    }

    /**
     * 服务器获取数据
     */
    public void getDataFromServe() {
        UserRelation userRelation = new UserRelationImpl();
        userRelation.getUserFollowingRelations(this.getContext(), okFollowingListener, errorFollowListener);
        userRelation.getUserFollowerRelations(this.getContext(), okFollowerListener, errorFollowListener);
    }

}
