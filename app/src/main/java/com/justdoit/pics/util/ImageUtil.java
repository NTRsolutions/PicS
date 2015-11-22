package com.justdoit.pics.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.justdoit.pics.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具
 * TODO 图片参数的添加
 * 1. bitmap转byte[]
 * 2. byte[]转bitmap
 * Created by mengwen on 2015/11/21.
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 简单的将bytes转成bitmap
     *
     * @param data 图片数据
     * @return 如果成功, 返回bitmap;否则返回null
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
     *
     * @param bitmap
     * @param quality 表示图片质量，范围0-100,100表示最好,图片最大
     * @return 如果成功, 返回bytes;否则返回null
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
                Log.e(TAG, "getBytesFromBitmap() failed");
            }

            return out.toByteArray();
        } else {
            return null;
        }
    }

    /**
     * 保存文件到外存的、app名字的文件夹
     *
     * @param context
     * @param bitmap
     * @param quality
     * @return 成功就返回图片路径;否则返回空字符串
     */
    public static String saveBitmap(Context context, Bitmap bitmap, int quality) {
        // 创建以应用名为文件夹名的文件夹
        File appDir = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name));

        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String filename = System.currentTimeMillis() + ".png"; // 以时间戳为名，保存为png格式
        File file = new File(appDir, filename);
        try {
            FileOutputStream out = new FileOutputStream(file);

            out.write(getBytesFromBitmap(bitmap, quality));

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "saveBitmap() failed");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getBytesFromBitmap() failed");
        }

//        if (file != null && file.exists()) {
//            return file.getPath();
//        } else {
//            return "";
//        }

        return file.getAbsolutePath();

    }
}
