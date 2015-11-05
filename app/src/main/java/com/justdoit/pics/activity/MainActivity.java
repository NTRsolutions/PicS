package com.justdoit.pics.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.mRecyclerViewAdapter;
import com.justdoit.pics.fragment.mainFragment;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 用来跳转到需要的测试页面
 * TODO:等待数据接口
 * Created by mengwen on 2015/10/26.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private App mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }



    private void initView() {
        mApp = (App)getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);




        setSupportActionBar(toolbar);
        if(App.isLogin()){//登录才能显示用户头像
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.isLogin()) {
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                } else {
                    // 带上要跳转到UserInfo的标识
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.ACTION_KEY, "UserInfoActivity");
                    startActivity(intent);
                }

            }
        });

        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, mainFragment.newInstance("test"),"mainFragment")
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
