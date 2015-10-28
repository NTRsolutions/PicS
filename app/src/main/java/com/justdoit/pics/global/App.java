package com.justdoit.pics.global;

import android.app.Application;
import android.util.Log;

import com.justdoit.pics.model.MyCookieStore;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by mengwen on 2015/10/27.
 */
public class App extends Application {

    public static CookieManager cookieManager;
    private final static String TAG = "App";


    @Override
    public void onCreate() {
        super.onCreate();

        setCookieManager(); // 设置cookie
    }

    /**
     * 初始化和设置cookie
     *
     * @return
     */
    public void setCookieManager() {

        // 初始化cookie
        if (cookieManager == null) {
            try {
                cookieManager = new CookieManager(new MyCookieStore(this, new URI(Constant.HOME_URL)), CookiePolicy.ACCEPT_ALL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.e(TAG, "new URI() failed");
            }
            CookieHandler.setDefault(cookieManager);
        }
    }

    /**
     * 获取相应uri的token值
     *
     * @param str
     * @return
     */
    public static String getToken(String str) {
        MyCookieStore cookieStore = (MyCookieStore) cookieManager.getCookieStore();
        URI uri = null;

        try {
            uri = new URI(str);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "App new URI() failed");
        }

        return cookieStore.getToken(uri);
    }
}
