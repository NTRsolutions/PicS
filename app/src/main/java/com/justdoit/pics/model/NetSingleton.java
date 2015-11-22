package com.justdoit.pics.model;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 单例模式的网络队列类，可获取request queue和image loader
 * Created by mengwen on 2015/10/25.
 */
public class NetSingleton {

    private final String TAG = "DefaultRequestTAG"; // 请求默认标记

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
     *
     * @return 全局的queue
     */
    public RequestQueue getRequestQueue() {

        if (queue == null) {
            // 一般使用application 保证生命周期和app一致
            queue = Volley.newRequestQueue(mCxt.getApplicationContext());
        }
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
