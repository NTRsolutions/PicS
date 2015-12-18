package com.justdoit.pics.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.BriefIntroAdapter;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.App;
import com.justdoit.pics.util.CheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 接收从简介页面的adapter传递过来的参数:
 * String oldValue
 * String nameType 修改的参数的名称类型，具体参见BriefIntroAdapter的枚举类NAME_TYPE
 * <p/>
 * <p/>
 * TODO 没有处理性别、生日和居住地的修改
 * Created by mengwen on 2015/11/24.
 */
public class ChangeInfoActivity extends AppCompatActivity {


    private static final String TAG = "ChangeInfoActivity";
    private TextInputLayout inputLayout;
    private Resources res;

    private User user;

    private BriefIntroAdapter.NAME_TYPE type;

    private String newValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        res = getResources();
        user = new UserImpl();
        newValue = new String();

        initView();

        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_change_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                // 上传服务器
                if (CheckNewValue()) {
                    work();
                }
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        inputLayout = (TextInputLayout) findViewById(R.id.change_info_edt_layout);

        // 获取传递过来的数据
        Bundle data = getIntent().getExtras();

        if (data == null) {
            // 没有数据，提前结束函数
            return;
        }

        // date不为null
        String oldValue = data.getString("oldValue");  // 之前的参数
        int nameType = data.getInt("nameType"); // 修改的参数的名称类型，具体参见BriefIntroAdapter的枚举类NAME_TYPE
        type = BriefIntroAdapter.NAME_TYPE.values()[nameType];

        inputLayout.getEditText().setText(oldValue);

        switch (type) {
            case nickname:
                inputLayout.setHint(res.getString(R.string.nick_name));
                break;
            case email:
                inputLayout.setHint(res.getString(R.string.prompt_email));
                break;
            case sex:
                inputLayout.setHint(res.getString(R.string.sex));
                break;
            case residence:
                inputLayout.setHint(res.getString(R.string.residence));
                break;
            case birthday:
                inputLayout.setHint(res.getString(R.string.birthday));
                break;
            default:
                inputLayout.setHint(type.getName());
                break;
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.change_info_toolbar);
        toolbar.setTitle(res.getStringArray(R.array.change_info)[nameType]);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 设置返回键
    }

    private Response.ErrorListener errorListener;
    private Response.Listener okListener;

    /**
     * 初始化网络监听器
     */
    public void initListener() {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.toString());
            }
        };

        okListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e(TAG, String.valueOf(response) + "");
                finish();
            }
        };
    }

    /**
     * 向服务器传递修改的参数
     * TODO 没有处理性别、生日和居住地的修改
     */
    public void work() {

        Map<String, String> params = new HashMap<String, String>();
        switch (type) {
            case nickname:
                params.put(BriefIntroAdapter.NAME_TYPE.nickname.getName(), newValue);
                break;
            case email:
                params.put(BriefIntroAdapter.NAME_TYPE.email.getName(), newValue);
                break;
            default:
                break;
        }
        // 处理
        user.changeUserInfo(this, App.getUserId(), params, null, okListener, errorListener);

    }

    /**
     * @return true:符合格式,启动work() 访问服务器
     * false:不符合格式
     */
    private boolean CheckNewValue() {
        inputLayout.setError(null);

        newValue = String.valueOf(inputLayout.getEditText().getText());

        if (TextUtils.isEmpty(newValue)) {
            inputLayout.setError(res.getString(R.string.error_field_required));
            return false;
        } else {
            switch (type) {
                case nickname:
                    // TODO nick name 字符限制
                    if (!CheckUtil.isUsernameValid(newValue)) {
                        inputLayout.setError(res.getString(R.string.error_invalid_username));
                        return false;
                    } else {
                        return true;
                    }
                case email:
                    if (!CheckUtil.isEmailValid(newValue)) {
                        inputLayout.setError(res.getString(R.string.error_invalid_email));
                        return false;
                    } else {
                        return true;
                    }
                case sex:
                    // TODO 性别另外处理
                    return true;
                case birthday:
                    return true;
                case residence:
                    return true;
                default:
                    return false;
            }
        }
    }
}
