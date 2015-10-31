package com.justdoit.pics.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostFormJsonObjRequest;
import com.justdoit.pics.util.NetUtil;
import com.justdoit.pics.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过用户名和密码登录
 */
public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";

    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private View mProgressView;
    private View mLoginFormView;

    private NetSingleton mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {

        setSupportActionBar((Toolbar) findViewById(R.id.login_toolbar));
        getSupportActionBar().setTitle(R.string.action_sign_in);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mInstance = NetSingleton.getInstance(getApplicationContext());

    }


    /**
     * 检查登录信息是否符合
     */
    private void attemptLogin() {

        // 关闭软键盘
        SystemUtil.hideSystemKeyBoard(this, mSignInButton);

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else if (NetUtil.isNetworkAvailable(this)) {
            showProgress(true);
            work(username, password);
        } else {

            Toast.makeText(this, "当前没有网络哦(T_T)", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录操作，成功后把用户id和用户名保存在preference文件
     *
     * @param username
     * @param password
     */
    public void work(final String username, String password) {
        Map<String, String> map = new HashMap<String, String>();


        String token = App.getToken(Constant.HOME_URL);

        map.put(Constant.TOKEN_NAME, token);
        map.put("username", username);
        map.put("password", password);

        if (NetUtil.isNetworkAvailable(this)) {
            PostFormJsonObjRequest request = new PostFormJsonObjRequest(
                    Constant.HOME_URL + Constant.LOGIN_URL_SUFFIX, map,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            showProgress(false);

                            try {
                                // 登录用户id和用户名信息写入shared preference文件
                                JSONObject jsonObject = new JSONObject(response.toString());
                                SharedPreferences.Editor editor = getSharedPreferences(Constant.USER_INFO_PREFS, MODE_PRIVATE).edit();

                                editor.putInt(Constant.USER_ID_NAME, jsonObject.getInt(Constant.USER_ID_NAME));
                                editor.putString(Constant.USERNAME_NAME, username);
                                editor.commit();
                                App.setUserId(jsonObject.getInt(Constant.USER_ID_NAME)); // 设置全局userId
                                App.setUserName(username);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "new JSONObject() failed");
                            }

                            goActivity();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                            showProgress(false);

                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                        }
                    }
            );


            mInstance.addToRequestQueue(request);
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
            intent = new Intent(LoginActivity.this, UserInfoActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private boolean isUsernameValid(String username) {
        // TODO 需要正则表达式
        return username.length() < 30;

    }

    private boolean isPasswordValid(String password) {
        // TODO 需要商量一下
        return password.length() >= 6;
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

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

