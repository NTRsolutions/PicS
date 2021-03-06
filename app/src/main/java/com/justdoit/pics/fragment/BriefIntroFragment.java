package com.justdoit.pics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justdoit.pics.R;
import com.justdoit.pics.adapater.BriefIntroAdapter;
import com.justdoit.pics.bean.UserFollowerListInfo;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.bean.UserFollowingListInfo;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        UserInfo userInfo;

        boolean isUserOwn = true;

        if (bundle != null) {
            userInfo = (UserInfo) bundle.getSerializable(USER_INFO);
            isUserOwn = bundle.getBoolean(IS_USER_OWN);
        } else {
            userInfo = new UserInfo();
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

    public void updateFollowings(UserFollowingListInfo followings) {
        briefIntroAdapter.setFollowings(followings);
        briefIntroAdapter.notifyDataSetChanged();
    }

    public void updateFollowers(UserFollowerListInfo followers) {
        briefIntroAdapter.setFollowers(followers);
        briefIntroAdapter.notifyDataSetChanged();
    }
}
