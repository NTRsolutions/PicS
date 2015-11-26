package com.justdoit.pics.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.justdoit.pics.R;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.util.CheckUtil;
import com.justdoit.pics.util.NetUtil;
import com.justdoit.pics.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过用户名和密码登录
 * 或者通过用户名、email和密码注册
 * <p/>
 * <p/>
 * 其中goActivity()方法控制跳转到目标的activity
 */
public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";
    // key值
    private final String USER_NAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email";

    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mEmailLayout;

    private TextView mHintTextView; // 提示转换登录和注册

    private Button mButton;
    private View mProgressView;
    private View mProgressLayoutView;
    private View mLoginFormView;

    private boolean isToLogin = true; // 是准备登录

    private NetSingleton mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // up事件处理
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化view
     */
    private void initView() {

        setSupportActionBar((Toolbar) findViewById(R.id.login_toolbar));
        getSupportActionBar().setTitle(R.string.action_login_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 返回上一级

        mHintTextView = (TextView) findViewById(R.id.hint_tv);
        mHintTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        mHintTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isToLogin = !isToLogin;

                if (isToLogin) {
                    ((TextView) v).setText(R.string.register_hint);
                    mButton.setText(R.string.action_login_in);
                    getSupportActionBar().setTitle(R.string.action_login_in);
                    mEmailLayout.setVisibility(View.GONE);
                } else {
                    ((TextView) v).setText(R.string.login_hint);
                    mButton.setText(R.string.action_sign_up);
                    getSupportActionBar().setTitle(R.string.action_sign_up);
                    mEmailLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        mUsernameLayout = (TextInputLayout) findViewById(R.id.username_layout);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        mEmailLayout = (TextInputLayout) findViewById(R.id.email_layout);

        // 点击清除错误信息

        mButton = (Button) findViewById(R.id.sign_in_button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mProgressLayoutView = findViewById(R.id.login_progress_layout);

        mInstance = NetSingleton.getInstance(getApplicationContext());

    }


    /**
     * 检查登录信息是否符合
     */
    private void attemptLogin() {

        // 关闭软键盘
        SystemUtil.hideSystemKeyBoard(this, mButton);

        mUsernameLayout.setError(null);
        mPasswordLayout.setError(null);
        mEmailLayout.setError(null);

        String username = mUsernameLayout.getEditText().getText().toString();
        String password = mPasswordLayout.getEditText().getText().toString();
        String email = mEmailLayout.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码
        if (TextUtils.isEmpty(password)) {
            mPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = mPasswordLayout;
            cancel = true;
        } else if (!CheckUtil.isPasswordValid(password)) {
            mPasswordLayout.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordLayout;
            cancel = true;
        }

        if (!isToLogin) {
            if (TextUtils.isEmpty(email)) {
                mEmailLayout.setError(getString(R.string.error_field_required));
                focusView = mEmailLayout;
                cancel = true;
            } else if (!CheckUtil.isEmailValid(email)) {
                mEmailLayout.setError(getString(R.string.error_invalid_email));
                focusView = mEmailLayout;
                cancel = true;
            }
        }

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            mUsernameLayout.setError(getString(R.string.error_field_required));
            focusView = mUsernameLayout;
            cancel = true;
        } else if (!CheckUtil.isUsernameValid(username)) {
            mUsernameLayout.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameLayout;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else if (NetUtil.isNetworkAvailable(this)) {
            showProgress(true);

            work(email, username, password);
        } else {
            Toast.makeText(this, "当前没有网络哦(T_T)", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录或者注册操作，主要通过isToLogin判断
     * true:登录
     * false:注册
     * 成功后把用户id和用户名保存在preference文件
     *
     * @param email
     * @param username
     * @param password
     */
    public void work(String email, final String username, String password) {
        final Map<String, String> map = new HashMap<String, String>();

        map.put(USER_NAME, username);
        map.put(PASSWORD, password);

        // 判断登录还是注册，改变url和传递数据
        if (NetUtil.isNetworkAvailable(this)) {
            final User user = new UserImpl();

            final Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // 显示错误信息

                    if (error.networkResponse != null) {
                        showErrorMessage(error.networkResponse);
                    } else {
                        Toast.makeText(LoginActivity.this, "错误都错误了=++=", Toast.LENGTH_LONG).show();
                    }

                    showProgress(false);
                }
            };
            final Response.Listener okLoginListener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    showProgress(false);
                    // 保存参数
                    if (response != null) {
                        saveUserInfo(String.valueOf(response), username);

                        // 跳转到相应页面
                        goActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "返回参数出错+-+", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Response.Listener okRegistListener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    user.login(LoginActivity.this, map
                            , okLoginListener, errorListener);
                }
            };

            if (!isToLogin) {
                map.put(EMAIL, email);
                user.regist(this, map, okRegistListener, errorListener);
            } else {
                user.login(this, map, okLoginListener, errorListener);
            }
        }

    }

    /**
     * 保存登录或注册成功后的信息
     * 直接保存填写的username
     *
     * @param jsonStr
     */
    private void saveUserInfo(String jsonStr, String username) {
        try {
            // 登录用户id和用户名信息写入shared preference文件
            JSONObject jsonObject = new JSONObject(jsonStr);
            SharedPreferences.Editor editor = getSharedPreferences(Constant.USER_INFO_PREFS, MODE_PRIVATE).edit();

            int userid = -1;

            userid = jsonObject.getInt(Constant.USER_ID_NAME);

            editor.putInt(Constant.USER_ID_NAME, userid);
            editor.putString(Constant.USERNAME_NAME, username);
            editor.commit();

            App.setUserId(userid); // 设置全局userId
            App.setUserName(username);

            SharedPreferences.Editor cookieEditor = getSharedPreferences(Constant.COOKIES_PREFS, MODE_PRIVATE).edit();
            for (HttpCookie c : App.cookieManager.getCookieStore().getCookies()) {
                cookieEditor.putString(c.getName(), c.getValue());
                Log.e(TAG, "name:" + c.getName() + " value:" + c.getValue());
            }

            cookieEditor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "saveUserInfo():new JSONObject() failed");
        }
    }

    /**
     * 显示登录或注册失败的返回信息
     *
     * @param response
     */
    private void showErrorMessage(NetworkResponse response) {
        String jsonStr = new String(response.data);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            // 显示各项错误
            if (!jsonObject.isNull(USER_NAME)) {
                // 如果username不是空
                String usernameError = jsonObject.getJSONArray(USER_NAME).getString(0);
                mUsernameLayout.setError(usernameError);
                mUsernameLayout.requestFocus();
            } else if (!jsonObject.isNull(PASSWORD)) {
                String passwordError = jsonObject.getJSONArray(PASSWORD).getString(0);
                mPasswordLayout.setError(passwordError);
                mPasswordLayout.requestFocus();
            } else if (!isToLogin && !jsonObject.isNull(EMAIL)) {
                String emailError = jsonObject.getJSONArray(EMAIL).getString(0);
                mEmailLayout.setError(emailError);
                mEmailLayout.requestFocus();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "showErrorMessage() : new JSONObject() failed");
        }
    }

    /**
     * 根据传递的值判断要启动的activity
     */
    private void goActivity() {

        String action = getIntent().getStringExtra(Constant.ACTION_KEY);
        Intent intent = null;

        if (action == null || TextUtils.isEmpty(action)) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        } else if (action.equals("UserInfoActivity")) {
            // 转载userid和username数据
            intent = new Intent(LoginActivity.this, UserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.USER_ID_NAME, App.getUserId());
            bundle.putString(Constant.USERNAME_NAME, App.getUserName());
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    }

    /**
     * 显示dialog和隐藏login form
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressLayoutView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

