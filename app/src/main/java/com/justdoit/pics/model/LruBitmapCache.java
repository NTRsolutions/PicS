package com.justdoit.pics.model;


import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by mengwen on 2015/10/26.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    /**
     * 默认配置
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    /**
     * 根据运行内存返回默认cache大小。
     *
     * @return cache大小（MB）
     */
    public static int getDefaultLruCacheSize() {
        int maxMemory = (int) ((Runtime.getRuntime().maxMemory() / 1024));
        int maxCache = maxMemory / 8;
        return maxCache;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return this.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        this.put(url, bitmap);
    }
}
