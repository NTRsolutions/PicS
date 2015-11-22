package com.justdoit.pics.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by mengwen on 2015/11/22.
 */
public class FileUtil {

    private final static String TAG = "FileUtil";

    /**
     * 由文件路径得出文件的扩展名,不带".";如: png
     * @param path
     * @return
     *    如果扩展名存在就返回扩展名(不带".");否则返回""空字符串
     */
    public static String getFileExtensionFromPath(String path) {
        String extension = "";

        if (!TextUtils.isEmpty(path)) {
            int l = path.lastIndexOf(".");
            if (l != -1 && (l + 1 <= path.length())) {
                extension = path.substring(l + 1, path.length());
            }
        }

        return extension;
    }

    /**
     * 通过文件路径,获取文件bytes
     * @param path 文件路径
     * @return
     *   文件bytes
     */
    public static byte[] getBytesFromPath(String path) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            int len = 0;
            byte[] temp = new byte[1024];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            while ((len = in.read(temp)) != -1) {
                out.write(temp);
            }

            in.close();
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "getBytesFromPath() failed");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getBytesFromPath() failed");
        }

        return out.toByteArray();
    }
}
