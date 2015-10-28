package com.justdoit.pics.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.justdoit.pics.R;

/**
 * 用来跳转到需要的测试页面
 * TODO:完成测试后，就要将list view的页面设置为主页面
 * Created by mengwen on 2015/10/26.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button userinfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        userinfoBtn = (Button) findViewById(R.id.user_info_btn);

        userinfoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
