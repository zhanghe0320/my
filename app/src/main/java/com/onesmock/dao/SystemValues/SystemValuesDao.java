package com.onesmock.dao.SystemValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;
import com.onesmock.dao.product.Product;

import java.util.ArrayList;

public class SystemValuesDao {

    // private SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象
    private DBOpenHelper dbOpenHelper;
    private static final String TAG = "SystemValuesDao";

    public SystemValuesDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        // sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }



    public SystemValues dbQueryOneByPass(String name) {
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_systemvalues where name = ?";
        String[] selectionArgs = new String[]{name};


        Cursor cursor = null;

        try {
            cursor =  dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);

            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                SystemValues systemValues = new SystemValues();
                systemValues.setId(cursor.getInt(cursor.getColumnIndex("id")));
                systemValues.setName(cursor.getString(cursor.getColumnIndex("name")));
                systemValues.setValue(cursor.getString(cursor.getColumnIndex("value")));
                systemValues.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                systemValues.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));


                return systemValues;// 返回总记录数
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;// 如果没有数据，则返回0
    }

    // 插入用户数据    private String id;  使用中
    public void dbInsert(String name, String value, String equipmenthost, String id) {
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        String sql = "update  t_systemvalues set name =? ,value =?,equipmenthost =? where id =? ";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[]{ name, value, equipmenthost.toLowerCase(), id};
        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }



    // 根据用户名查阅用户  正在使用
    public SystemValues dbQueryOneByID( String id) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_systemvalues where id =?";
        String[] selectionArgs = new String[]{id};


        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);

            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                // 如果有用户，则把查到的值填充这个用户实体
                SystemValues systemValues = new SystemValues();
                systemValues.setId(cursor.getInt(cursor.getColumnIndex("id")));
                systemValues.setName(cursor.getString(cursor.getColumnIndex("name")));
                systemValues.setValue(cursor.getString(cursor.getColumnIndex("value")));
                systemValues.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                systemValues.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                return systemValues;// 返回一个用户给前台
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG, sql);
        return null;// 没有返回null
    }

    // 根据用户名查阅用户
    public SystemValues dbQueryOneByName(String name) {
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_systemvalues where name =?";
        String[] selectionArgs = new String[]{name};


        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);

            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                // 如果有用户，则把查到的值填充这个用户实体
                SystemValues systemValues = new SystemValues();
                systemValues.setId(cursor.getInt(cursor.getColumnIndex("id")));
                systemValues.setName(cursor.getString(cursor.getColumnIndex("name")));
                systemValues.setValue(cursor.getString(cursor.getColumnIndex("value")));
                systemValues.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                systemValues.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                return systemValues;// 返回一个用户给前台
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG, sql);

        // cursor.close();
        return null;// 没有返回null
    }



    // 查询所有用户
    public ArrayList<SystemValues> dbQueryAll() {
        ArrayList<SystemValues> passList = new ArrayList<SystemValues>();
        // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_systemvalues";

        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);
            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

                SystemValues systemValues = new SystemValues();
                systemValues.setId(cursor.getInt(cursor.getColumnIndex("id")));
                systemValues.setName(cursor.getString(cursor.getColumnIndex("name")));
                systemValues.setValue(cursor.getString(cursor.getColumnIndex("value")));
                systemValues.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                systemValues.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));

                passList.add(systemValues);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return passList;
    }

    // 更新厂家设置信息  使用中
    public void dbUpdateValue(String value, Long CreatedTime, int id ) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();.
        String sql = null;
        Object bindArgs[];
        if(null == value || "" .equals(value)){
            bindArgs= new Object[]{CreatedTime, id};
            sql = "update t_systemvalues set value='',CreatedTime =?  where id=?";

        }else{
            bindArgs= new Object[]{value, CreatedTime, id};
            sql = "update t_systemvalues set value=?,CreatedTime =?  where id=?";
        }


        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }

    // 更新厂家设置信息  使用中 修改所有的设备主机号码
    public void dbUpdateEequipmenthost(String equipmenthostold,String equipmenthostnew, Long CreatedTime, int id ) {
        //    sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "update t_systemvalues set CreatedTime =? ,equipmenthost =? where equipmenthost !=? ";

        Log.i(TAG,"修改设备号码："+sql+"设备号码"+equipmenthostnew+"设备主机"+equipmenthostold+"id"+id);

        Object bindArgs[] = new Object[]{CreatedTime, equipmenthostnew.toLowerCase(),equipmenthostnew.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }

    // 更新厂家设置信息  使用中    修改使用的串口
    public void dbUpdateSerialPort(String value,String equipmenthost, Long CreatedTime, int id ) {
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "update t_systemvalues set value =?  where equipmenthost=? and id =? ";
        Log.i(TAG,"修改设备号码："+sql+"设备号码"+value+"设备主机"+equipmenthost+"id");
        Object bindArgs[] = new Object[]{value, equipmenthost.toLowerCase(),id};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }
    // 更新厂家设置信息  使用中  修改记录设备号的地方 进行更修
    public void dbUpdateEequipmenthost0(String equipmenthostold,String equipmenthostnew, Long CreatedTime, int id ) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "update t_systemvalues set value=?,equipmenthost =? where  id =? ";
        Log.i(TAG,"修改设备号码："+sql+"设备号码"+equipmenthostnew+"设备主机"+equipmenthostold+"id"+id);

        Object bindArgs[] = new Object[]{equipmenthostnew.toLowerCase(),equipmenthostnew.toLowerCase(),id};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }


}
