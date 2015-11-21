package com.justdoit.pics.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * 图片处理工具
 * TODO 图片参数的添加
 * 1. bitmap转byte[]
 * 2. byte[]转bitmap
 * Created by mengwen on 2015/11/21.
 */
public class ImageUtil {
    /**
     * 简单的将bytes转成bitmap
     * @param data 图片数据
     * @return
     *   如果成功,返回bitmap;否则返回null
     */
    public static Bitmap getBitmapFromBytes(byte[] data) {

        if (data != null) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            return null;
        }
    }

    /**
     * 简单的byte转成bitmap
     * @param bitmap
     * @return
     *   如果成功,返回bytes;否则返回null
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            return out.toByteArray();
        } else {
            return null;
        }
    }
}
