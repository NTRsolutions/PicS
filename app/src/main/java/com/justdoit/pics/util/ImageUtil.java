package com.justdoit.pics.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
     * @param quality 表示图片质量，范围0-100,100表示最好,图片最大
     * @return
     *   如果成功,返回bytes;否则返回null
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        if (bitmap != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out); // 100表示图片质量，范围0-100

            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return out.toByteArray();
        } else {
            return null;
        }
    }
}
