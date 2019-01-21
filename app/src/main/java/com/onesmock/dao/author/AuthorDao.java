package com.onesmock.dao.author;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;

import java.util.ArrayList;

public class AuthorDao {

    private DBOpenHelper dbOpenHelper;// 创建DBOpenHelper对象
   // private   SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象
    private static final String TAG = "AuthorDao";

    public AuthorDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        //sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }


    // 查询所有用户   在使用
    public ArrayList<Author> dbQueryAll() {
        ArrayList<Author> authorList = new ArrayList<Author>();
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_author";
        Cursor cursor = null;
        Log.i(TAG, "dbQueryAll: 查询所有员工");
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);
            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Author author = new Author();
                author.setAuthor_name(cursor.getString(cursor.getColumnIndex("author_name")));
                author.setAuthor_phone(cursor.getString(cursor.getColumnIndex("author_phone")));
                author.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                author.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                author.setId(cursor.getInt(cursor.getColumnIndex("id")));
                authorList.add(author);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return authorList;
    }


    // 修改   使用 修改主机所有辅助机器的设备号码
    public void dbDeleteAuthorEquipmenthost(String equipmenthostold) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "Delete  from t_author  where equipmenthost !=?";
        Log.i(TAG, "修改主机所有辅助机器的设备号码：" + equipmenthostold);
        Object bindArgs[] = new Object[]{equipmenthostold.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }

    // 修改   使用 删除所有数据
    public void dbDeleteAllAuthor(String equipmenthost ) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "Delete  from t_author where equipmenthost != ? ";
        Log.i(TAG, "删除所有员工：" + sql);
        Object bindArgs[] = new Object[]{equipmenthost};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


    // 查询数据  查询是否存在这条数据  如果存在打回去  不做添加   使用中
    public Author dbQueryOne(String author_phone, String author_name) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_author where author_phone =? and author_name =?";
        String bindArgs[] = new String[]{author_phone, author_name};
        Cursor cursor = null;
        Author author = null;
        Log.i(TAG, "dbQueryOne: 查询员工是否存在"+author_name+author_phone);
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, bindArgs);
            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                author = new Author();
                author.setAuthor_name(cursor.getString(cursor.getColumnIndex("author_name")));
                author.setAuthor_phone(cursor.getString(cursor.getColumnIndex("author_phone")));
                author.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                author.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                author.setId(cursor.getInt(cursor.getColumnIndex("id")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return author;// 如果没有数据，则返回0
    }


    // 修改   使用 添加工作人员
    public void dbInsertAuthor(String author_phone, String author_name, String equipmenthost) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();


        String sql = "insert into t_author (author_name,author_phone,equipmenthost)values (?,?,?)";
        Log.i(TAG, "添加工作人员：" + sql);
        Object bindArgs[] = new Object[]{author_name, author_phone, equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }

}
