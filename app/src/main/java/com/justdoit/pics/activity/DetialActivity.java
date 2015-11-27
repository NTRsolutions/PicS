package com.justdoit.pics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.justdoit.pics.R;
import com.justdoit.pics.fragment.DetialActivityFragment;
import com.justdoit.pics.fragment.MainFragment;

public class DetialActivity extends AppCompatActivity {

    int pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        pk = i.getIntExtra("pk",-1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_detial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, DetialActivityFragment.newInstance(pk), "DetialActivityFragment")
                .commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击up按钮事件处理
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
