package com.smasher.oa.core.bitmap;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import com.smasher.oa.core.log.Logger;
import com.smasher.oa.core.other.ApplicationContext;


/**
 * @author moyu
 * @date 2017/4/8
 */

public class BitmapManager {
    private static int MAX_MEMORY_CACHE_SIZE = 10 * 1024 * 1024;
    private static MemoryCache LruMemoryCache;

    static {
        LruMemoryCache = createMemoryCache(ApplicationContext.getInstance(), MAX_MEMORY_CACHE_SIZE);
    }

    /**
     * Creates default implementation of {@link MemoryCache} - {@link LruMemoryCache}<br />
     * Default cache size = 1/8 of available app memory.
     */
    public static MemoryCache createMemoryCache(Context context, int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            if (hasHoneycomb() && isLargeHeap(context)) {
                memoryClass = getLargeMemoryClass(am);
            }
            memoryCacheSize = 1024 * 1024 * memoryClass / 8;
        }
        return new LruMemoryCache(memoryCacheSize);
    }

    private static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isLargeHeap(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static int getLargeMemoryClass(ActivityManager am) {
        return am.getLargeMemoryClass();
    }

    /**
     * 获取bitmap大小
     *
     * @param bitmap
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getBitmapSize(Bitmap bitmap) {
        //4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        //3.1及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        //3.1之前的版本
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 加入内存缓存
     *
     * @param tag
     * @param bitmap
     */
    public static void addBitmapToCache(final String tag, final Bitmap bitmap) {
        if (TextUtils.isEmpty(tag) || bitmap == null || bitmap.isRecycled()) {
            return;
        }

        try {
            //先加入内存缓存
            if (LruMemoryCache != null) {
                LruMemoryCache.put(tag, bitmap);
            }
        } catch (Exception ex) {
            Logger.exception(ex);
        }
    }

    /**
     * 从缓存中获取图片
     *
     * @param tag
     * @return
     */
    public static Bitmap getBitmapFromCache(final String tag) {
        Bitmap bitmap = LruMemoryCache == null ? null : LruMemoryCache.get(tag);
        return bitmap;
    }

    /**
     * 从缓存中移除图片
     *
     * @param tag
     */
    public static void removeBitmap(final String tag) {
        if (LruMemoryCache != null) {
            LruMemoryCache.remove(tag);
        }
    }

    /**
     * 清理缓存
     */
    public static void clearBitmapCache() {
        if (LruMemoryCache != null) {
            LruMemoryCache.clear();
        }
    }
}
