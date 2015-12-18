package com.justdoit.pics.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * 单例模式的网络队列类，可获取request queue和image loader
 * Created by mengwen on 2015/10/25.
 */
public class NetSingleton {

    private final String TAG = "DefaultRequestTAG"; // 请求默认标记

    private static final String CACHE_DIR = "pics"; // 缓存文件夹名称

    private static final int MAX_CACHE_SIZE_IN_BYTES = 25 * 1024 * 1024; // 最大缓存为25M

    private static NetSingleton mInstance;

    private RequestQueue queue;
    private ImageLoader mImageLoader;
    private LruBitmapCache lruBitmapCache;
    private Context mCxt;

    private NetSingleton(Context context) {
        mCxt = context;

        queue = getRequestQueue();

        lruBitmapCache = new LruBitmapCache();

        mImageLoader = new ImageLoader(queue, lruBitmapCache);
    }

    /**
     * 获取NetSingleton单例
     *
     * @param context
     * @return NetSingleton单例
     */
    public synchronized static NetSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetSingleton(context.getApplicationContext()); // 使用全局context
        }

        return mInstance;
    }

    /**
     * 获取request queue
     * 创建外部存储的cache文件夹
     * @return 全局的queue
     */
    public RequestQueue getRequestQueue() {

        if (queue == null) {
            File rootDir = mCxt.getExternalCacheDir();
            if (rootDir == null) {
                Log.e(TAG, "It can't get external cache dir.");
                rootDir = mCxt.getCacheDir(); // 获取文件系统应用的cache，内存不够时，很容易被系统删除
            }

            File cacheDir = new File(rootDir, CACHE_DIR); // 缓存文件夹

            // 创建文件夹
            cacheDir.mkdirs();
            Cache diskCache = new DiskBasedCache(cacheDir, MAX_CACHE_SIZE_IN_BYTES);
            Network network = new BasicNetwork(new HurlStack());

            queue = new RequestQueue(diskCache, network);

            queue.start(); // 启动请求队列
        }

        queue = Volley.newRequestQueue(mCxt);

        return queue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        addToRequestQueue(request, null);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        getRequestQueue().add(request);
    }
}
