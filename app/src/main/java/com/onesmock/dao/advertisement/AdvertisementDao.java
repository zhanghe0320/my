package com.onesmock.dao.advertisement;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.DBUtil.DBOpenHelper;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;

import java.util.ArrayList;

public class AdvertisementDao {

    private DBOpenHelper dbOpenHelper;// 创建DBOpenHelper对象
    // private   SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象
    private static final String TAG = "AuthorDao";

    public AdvertisementDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        //sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }



    // 查询数据  查询是否存在这条数据  如果存在打回去  不做添加   使用中
    public Advertisement dbQueryOne(String goods_img_id, String adv_url) {
        // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_advertisement where goods_img_id =? and adv_url =?";
        String bindArgs[] = new String[]{goods_img_id, adv_url};
        Cursor cursor = null;
        Advertisement advertisement = null;
        Log.i(TAG, "dbQueryOne: 查询图片是否存在"+goods_img_id+"+++++++++++++"+adv_url);
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, bindArgs);
            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                advertisement = new Advertisement();

                advertisement.setId(cursor.getInt(cursor.getColumnIndex("id")));
                advertisement.setGoods_img_id(cursor.getString(cursor.getColumnIndex("goods_img_id")));
                advertisement.setAvd_url(cursor.getString(cursor.getColumnIndex("adv_url")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return advertisement;// 如果没有数据，则返回0
    }



    // 修改   使用 添加工作人员
    public void dbInsertAdvertisement(String goods_img_id, String adv_url) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();


        String sql = "insert into t_advertisement (goods_img_id,adv_url)values (?,?)";
        Log.i(TAG, "添加产品图册：" + sql);
        Object bindArgs[] = new Object[]{goods_img_id,adv_url};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }



    // 查询所有用户   在使用
    public ArrayList dbQueryAll() {
        ArrayList<String> advertisementArrayList = new ArrayList<String>();
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_advertisement";
        Cursor cursor = null;
        Log.i(TAG, "dbQueryAll: 查询所有图片地址");
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);
            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Advertisement advertisement = new Advertisement();
                advertisement.setId(cursor.getInt(cursor.getColumnIndex("id")));
                advertisement.setGoods_img_id(cursor.getString(cursor.getColumnIndex("goods_img_id")));
                advertisement.setAvd_url(cursor.getString(cursor.getColumnIndex("adv_url")));
                String url = ConstantValue.https+ xmppConnect.getOpenfireIpAddress()+advertisement.getAvd_url();
                advertisementArrayList.add(url);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return advertisementArrayList;
    }

    // 修改   使用 修改主机所有辅助机器的设备号码
    public void dbDeleteAllAdvertisement(String equipmenthost ) {
        // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "Delete  from t_advertisement ";
        Log.i(TAG, "删除所有广告：" + sql);
        Object bindArgs[] = new Object[]{};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


}
