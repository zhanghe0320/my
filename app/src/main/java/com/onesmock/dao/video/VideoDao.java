package com.onesmock.dao.video;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;


import java.util.ArrayList;

public class VideoDao {

    private DBOpenHelper dbOpenHelper;// 创建DBOpenHelper对象
    // private   SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象
    private static final String TAG = "VideoDao";

    public VideoDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        //sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }



    // 查询数据  查询是否存在这条数据  如果存在打回去  不做添加   使用中
    public Video dbQueryOne( String adv_url) {
        // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_video where  adv_url =?";
        String bindArgs[] = new String[]{ adv_url};
        Cursor cursor = null;
        Video video = null;
        Log.i(TAG, "dbQueryOne: 查询视频是否存在+++++++++++++"+adv_url);
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, bindArgs);
            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                video = new Video();

                video.setId(cursor.getInt(cursor.getColumnIndex("id")));
                video.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                video.setAvd_url(cursor.getString(cursor.getColumnIndex("adv_url")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return video;// 如果没有数据，则返回0
    }



    // 修改   使用 添加工作人员
    public void dbInsertVideo(String adv_url,String euipmenthost) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();


        String sql = "insert into t_video (adv_url,equipmenthost)values (?,?)";
        Log.i(TAG, "添加产品视频：" + sql);
        Object bindArgs[] = new Object[]{adv_url,euipmenthost};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }



    // 查询所有用户   在使用
    public ArrayList dbQueryAll() {
        ArrayList<String> videoArrayList = new ArrayList<String>();
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_video";
        Cursor cursor = null;
        Log.i(TAG, "dbQueryAll: 查询所有视频");
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);
            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Video video = new Video();
                video.setId(cursor.getInt(cursor.getColumnIndex("id")));
                video.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                video.setAvd_url(cursor.getString(cursor.getColumnIndex("adv_url")));

                videoArrayList.add(  video.getAvd_url());

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videoArrayList;
    }

    // 修改   使用 修改主机所有辅助机器的设备号码
    public void dbDeleteAllVideo(String equipmenthost) {
        // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "Delete  from t_video";
        Log.i(TAG, "删除所有视频：" + sql);
        Object bindArgs[] = new Object[]{equipmenthost};
        dbOpenHelper.getWritableDatabase().execSQL(sql);

    }

}
