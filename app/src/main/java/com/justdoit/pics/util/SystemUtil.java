package com.justdoit.pics.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mengwen on 2015/10/31.
 */
public class SystemUtil {

    /**
     * 关闭显示软键盘,但是可编辑区域的焦点还在,如果点击其他不可编辑控件,就会弹出软键盘
     * @param mContext
     */
    public static void closeKeyBoard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS); // 没有显示软键盘则显示
        }
    }

    /**
     * 关闭显示的软键盘,但是可编辑区域的焦点还在,除非点击可编辑区域,否则不会显示软键盘
     * @param mContext
     * @param v 随便一个view
     */
    public static void hideSystemKeyBoard(Context mContext, View v) {
        InputMethodManager imm = (InputMethodManager) (mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 判断是否拥有相机功能
     * @param mContext
     * @return
     *   true:有拍照功能
     *   false:没有拍照功能
     */
    public static boolean hasCamera(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
