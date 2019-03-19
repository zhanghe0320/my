package com.onesmock.dao.product;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;

public class ProductMessDao {




    //  private SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象

    private DBOpenHelper dbOpenHelper;
    private static final String TAG = "ProductMessDao";

    public ProductMessDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        //sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }



    // 插入产品的详情信息
    public void dbInsert(String equipmenthost, String equipmentbase) {
        //  sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        String sql = "insert into t_product(equipmenthost,equipmentbase) values (?,?)";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] { equipmenthost.toLowerCase(), equipmentbase.toLowerCase() };
        Log.i(TAG, "dbInsert: 插入数据信息");

        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }
    // 查询是否存在某样产品的信息
    public int dbQueryOne(String  imgUrl,String videoUrl,String  equipmentbase) {
        //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "select count(*) from t_product where imgUrl=? and equipmenthost =? and videoUrl =?";

        Log.i(TAG,"查询是否存在某产品："+sql);

        String bindArgs[] = new String[] { imgUrl,equipmentbase.toLowerCase(),videoUrl};

        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, bindArgs);


            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                return cursor.getInt(0);// 返回总记录数
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return 0;// 如果没有数据，则返回0

    }
}
