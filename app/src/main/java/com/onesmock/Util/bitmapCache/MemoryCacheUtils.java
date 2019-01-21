package com.onesmock.Util.bitmapCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存工具类
 *
 * @author Ace
 * @date 2016-02-19
 */
public class MemoryCacheUtils {


    // Android 2.3 (API Level
    // 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠,建议用LruCache,它是强引用

    private LruCache<String, Bitmap> mCache;

    public MemoryCacheUtils() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();// 获取虚拟机分配的最大内存
        // 16M
        // LRU 最近最少使用, 通过控制内存不要超过最大值(由开发者指定), 来解决内存溢出,就像上面翻译的所说 如果cache满了会清理最近最少使用的缓存对象
        mCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 计算一个bitmap的大小
                int size = value.getRowBytes() * value.getHeight();// 每一行的字节数乘以高度
                return size;
            }
        };
    }

    public Bitmap getBitmapFromMemory(String url) {

        return mCache.get(url);
    }

    public void setBitmapToMemory(String url, Bitmap bitmap) {

        mCache.put(url, bitmap);
    }

}