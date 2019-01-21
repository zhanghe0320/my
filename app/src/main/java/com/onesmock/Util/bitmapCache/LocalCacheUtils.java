package com.onesmock.Util.bitmapCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存工具类
 *
 * @author Ace
 * @date 2016-02-19
 */
public class LocalCacheUtils {


    // 图片缓存的文件夹
    public static final String DIR_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/ace_bitmap_cache";

    public Bitmap getBitmapFromLocal(String url) {
        try {
            File file = new File(DIR_PATH, MD5Encoder.encode(url));

            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setBitmapToLocal(Bitmap bitmap, String url) {
        File dirFile = new File(DIR_PATH);

        // 创建文件夹 文件夹不存在或者它不是文件夹 则创建一个文件夹.mkdirs,mkdir的区别在于假如文件夹有好几层路径的话,前者会创建缺失的父目录 后者不会创建这些父目录
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dirFile.mkdirs();
        }

        try {
            File file = new File(DIR_PATH, MD5Encoder.encode(url));
            // 将图片压缩保存在本地,参1:压缩格式;参2:压缩质量(0-100);参3:输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
