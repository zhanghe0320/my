package com.onesmock.Util.bitmapCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络缓存
 *
 * @author Ace
 * @date 2016-02-18
 */
public class NetCacheUtils {

    private LocalCacheUtils mLocalUtils;
    private MemoryCacheUtils mMemoryUtils;

    public NetCacheUtils(LocalCacheUtils localUtils,
                         MemoryCacheUtils memoryUtils) {
        mLocalUtils = localUtils;
        mMemoryUtils = memoryUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        BitmapTask task = new BitmapTask();
        task.execute(imageView, url);
    }

    /**
     * AsyncTask是线程池+handler的封装 第一个泛型: 传参的参数类型类型(和doInBackground一致) 第二个泛型:
     * 更新进度的参数类型(和onProgressUpdate一致) 第三个泛型: 返回结果的参数类型(和onPostExecute一致,
     * 和doInBackground返回类型一致)
     */
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView mImageView;
        private String url;

        // 主线程运行, 预加载
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 子线程运行, 异步加载逻辑在此方法中处理
        @Override
        protected Bitmap doInBackground(Object... params) {
            mImageView = (ImageView) params[0];
            url = (String) params[1];
            mImageView.setTag(url);// 将imageView和url绑定在一起
            // publishProgress(values)//通知进度

            // 下载图片
            return download(url);
        }

        // 主线程运行, 更新进度
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        // 主线程运行, 更新主界面
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // 判断当前图片是否就是imageView要的图片, 防止listview重用导致的图片错乱的情况出现
                String bindUrl = (String) mImageView.getTag();
                if (bindUrl.equals(url)) {
                    // 给imageView设置图片
                    mImageView.setImageBitmap(result);
                    // 将图片保存在本地
                    mLocalUtils.setBitmapToLocal(result, url);

                    // 将图片保存在内存
                    mMemoryUtils.setBitmapToMemory(url, result);
                }
            }
        }

    }

    /**
     * 下载图片
     *
     * @param url
     */
    public Bitmap download(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL(url).openConnection());

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream in = conn.getInputStream();
                // 将流转化为bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }


}