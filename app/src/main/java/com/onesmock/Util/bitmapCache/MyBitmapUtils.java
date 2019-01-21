package com.onesmock.Util.bitmapCache;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.onesmock.R;

/**
 * 三级缓存工具类
 *
 * @author Ace
 * @date 2016-02-19
 */
public class MyBitmapUtils {

    // 网络缓存工具类
    private static NetCacheUtils mNetUtils;
    // 本地缓存工具类
    private static LocalCacheUtils mLocalUtils;
    // 内存缓存工具类
    private static MemoryCacheUtils mMemoryUtils;

    public MyBitmapUtils() {
        mMemoryUtils = new MemoryCacheUtils();
        mLocalUtils = new LocalCacheUtils();
        mNetUtils = new NetCacheUtils(mLocalUtils, mMemoryUtils);
    }

    public static void display(ImageView imageView, String url) {
        // 设置默认加载图片
        imageView.setImageResource(R.drawable.updateapp);

        // 先从内存缓存加载
        Bitmap bitmap = mMemoryUtils.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            System.out.println("从内存读取图片啦...");
            return;
        }

        // 再从本地缓存加载
        bitmap = mLocalUtils.getBitmapFromLocal(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            System.out.println("从本地读取图片啦...");
            // 给内存设置图片
            mMemoryUtils.setBitmapToMemory(url, bitmap);
            return;
        }

        // 从网络缓存加载
        mNetUtils.getBitmapFromNet(imageView, url);
    }

}