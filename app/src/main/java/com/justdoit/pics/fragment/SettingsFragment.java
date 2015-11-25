package com.justdoit.pics.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.justdoit.pics.R;
import com.justdoit.pics.activity.MainActivity;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.App;

import java.awt.font.TextAttribute;

/**
 * Created by mengwen on 2015/11/24.
 */
public class SettingsFragment extends PreferenceFragmentCompat{

    private static final String TAG = "SettingsFragment";

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.super_pref);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);


        Button logoutBtn = new Button(view.getContext());
        logoutBtn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        logoutBtn.setText("退出登录");
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new UserImpl();
                user.logout(v.getContext(),
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Log.e(TAG, String.valueOf(response));
                                App.logout(getActivity()); // 登出删除信息
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.getMessage());
                            }
                        });
            }
        });

        view.addView(logoutBtn);

        return view;
    }
}
