package com.justdoit.pics.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * Created by mengwen on 2015/10/26.
 */
public class NetUtil {

    /**
     * 获取当前网络状态
     *
     * @param context
     * @return true:可用
     * false:不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
