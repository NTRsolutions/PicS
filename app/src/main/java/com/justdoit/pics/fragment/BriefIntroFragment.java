package com.justdoit.pics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.BriefIntroAdapter;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 简介fragment
 * Created by mengwen on 2015/11/10.
 */
public class BriefIntroFragment extends Fragment {
    private RecyclerView briefIntroList;

    private BriefIntroAdapter briefIntroAdapter;

    /**
     * 创建简介fragment
     *
     * @return
     *   简介fragment
     */
    public static BriefIntroFragment newInstance() {

        Bundle args = new Bundle();

        BriefIntroFragment fragment = new BriefIntroFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brief_intro, container, false);

        initView(view); // 初始化view

        getDataFromServer(); // 获取数据

        return view;
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view) {

        briefIntroList = (RecyclerView) view.findViewById(R.id.brief_intro_list);

        briefIntroList.setLayoutManager(new LinearLayoutManager(view.getContext())); // 设置线性布局

        briefIntroAdapter = new BriefIntroAdapter();

        briefIntroList.setAdapter(briefIntroAdapter); // 设置adapter
    }

    /**
     * 获取服务器用户数据
     */
    private void getDataFromServer() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.HOME_URL + Constant.USER_INFO_URL_SUFFIX + App.getUserId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<UserInfo>(){}.getType();

                        briefIntroAdapter.setUserInfo((UserInfo)gson.fromJson(String.valueOf(response), type));
                        briefIntroAdapter.notifyDataSetChanged(); // 更新界面
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        NetSingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
