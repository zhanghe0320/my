package com.onesmock.dao.equipment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;

import java.util.ArrayList;

public class EquipmentDao {
  //  private SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象

    private DBOpenHelper dbOpenHelper;
    private static final String TAG = "EquipmentDao";

    public EquipmentDao(Context context)// 定义构造函数
    {
        dbOpenHelper = DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
       // sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }


    // 插入用户数据    使用中
    public void dbInsertEquipment( String equipmentname,String equipmentbase,String equipmenthost) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        Long time=  System.currentTimeMillis();//CreatedTime =?

        String sql = "insert into t_equipment (equipmentname,equipmentbase,equipmenthost,CreatedTime)values (?,?,?,?)";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] {equipmentname,equipmentbase.toLowerCase(),equipmenthost.toLowerCase(),time};
        // 执行这条无返回值的sql语句
        Log.i(TAG, "dbInsertEquipment: 添加设备"+equipmentbase+equipmenthost+equipmentname);
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }


    // 插入用户数据    使用中
    public void dbUpdateEquipmentToNew( String equipmentname,String equipmentbase,String equipmenthost,String equipmentbaseold) {
        //sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        Long time=  System.currentTimeMillis();//CreatedTime =?

        String sql = "update  t_equipment set equipmentbase =?,equipmenthost =?,CreatedTime =?,equipmentid =0 where equipmentbase =?";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Log.i(TAG, "dbUpdateEquipmentToNew:修改主机信息 "+equipmentbase+"********"+equipmenthost+"********"+equipmentbaseold);
        Object bindArgs[] = new Object[] {equipmentbase.toLowerCase(),equipmenthost.toLowerCase(),time,equipmentbaseold.toLowerCase()};
        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }


    // 求出表中有多少条数据   使用中
    public int dbGetEquipmentSize() {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select count(*) from t_equipment";
        Cursor cursor = null;

        Log.i(TAG, "dbGetEquipmentSize: 查询有多少个设备，主机货架");
        try {
             cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);
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


    // 查询数据  使用中   查询货架是否存在
    public int dbQueryEquipment(String equipmenthost,String equipment_base) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select count(*) from t_equipment where equipmenthost=? and equipmentbase=?";
        String bindArgs[] = new String[] {equipmenthost.toLowerCase(),equipment_base.toLowerCase()};

        Log.i(TAG, "dbQueryEquipment: 通过主机，货架，查询是否存在此货架"+equipment_base+equipmenthost);
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



    // 根据用户名查阅用户   使用中
    public Equipment dbQueryOneByEquipmentname(String equipmentname) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_equipment where equipmentname =? ";
        String[] selectionArgs = new String[] { equipmentname };
        Log.i(TAG, "dbQueryOneByEquipmentname: 通过设备的名字查询设备！");
        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);
            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                // 如果有用户，则把查到的值填充这个用户实体
                Equipment equipment = new Equipment();

                equipment.setEquipmentbase(cursor.getString(cursor.getColumnIndex("equipmentbase")));
                equipment.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                equipment.setEquipmentName(cursor.getString(cursor.getColumnIndex("equipmentname")));
                equipment.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                equipment.setEquipmentid(cursor.getString(cursor.getColumnIndex("equipmentid")));
                equipment.setId(cursor.getInt(cursor.getColumnIndex("id")));


                return equipment;// 返回一个用户给前台
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return null;// 没有返回null
    }

    // 根据用户名查阅用户   在使用
    public Equipment dbQueryOneByEquipmentbase(String equipmentbase) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_equipment where equipmentbase =? ";
        String[] selectionArgs = new String[] { equipmentbase.toLowerCase() };
        Log.i(TAG, "dbQueryOneByEquipmentbase: 通过货架号码查找设备");
        Cursor cursor = null;

        Equipment equipment = null;
        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);
            if (cursor.moveToNext())// 判断Cursor中是否有数据
            {
                // 如果有用户，则把查到的值填充这个用户实体
                 equipment = new Equipment();

                equipment.setEquipmentbase(cursor.getString(cursor.getColumnIndex("equipmentbase")));
                equipment.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                equipment.setEquipmentName(cursor.getString(cursor.getColumnIndex("equipmentname")));
                equipment.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                equipment.setEquipmentid(cursor.getString(cursor.getColumnIndex("equipmentid")));
                equipment.setId(cursor.getInt(cursor.getColumnIndex("id")));


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return equipment;// 返回一个用户给前台
    }



    // 修改   使用
    public void dbUpdateEquipment(String equipmentbase,String  equipmenthost,Long CreatedTime,int equipmentid) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "update t_equipment set equipmentbase=? ,CreatedTime=?  where id =? and equipmenthost=?";
        Log.i(TAG,"更新设备的："+sql+"设备号码"+equipmentbase+"设备主机"+equipmenthost+"id"+equipmentid);
        Object bindArgs[] = new Object[] {equipmentbase.toLowerCase(),CreatedTime, equipmentid,equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }

    // 修改   修改设备ID字段
    public void dbUpdateEquipmentId(String equipmentbase,String  equipmenthost,String equipmentid) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        Long time=  System.currentTimeMillis();//CreatedTime =?

        String sql = "update t_equipment set  equipmentid =? where equipmentbase=? and equipmenthost=?";
        Log.i(TAG,"更新二维码使用的ID："+sql+"设备号码"+equipmentbase+"设备主机"+equipmenthost+"id"+equipmentid);
        Object bindArgs[] = new Object[] {equipmentid,equipmentbase.toLowerCase(),equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }



    // 修改   使用 修改主机所有辅助机器的设备号码
    public void dbDeleteEquipmentEquipmenthost(String equipmenthostnew) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete from t_equipment  where equipmenthost !=?";
        Log.i(TAG,"删除原来的设备："+sql+"设备号码"+equipmenthostnew+"设备主机"+"id");
        Object bindArgs[] = new Object[] {equipmenthostnew};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }


    // 查询数据  查询是否存在这条数据  如果存在打回去  不做添加   使用中
    public int dbQueryOne(String equipmenthost,String equipmentbase) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select count(*) from t_equipment where equipmenthost=? and equipmentbase =?";
        String bindArgs[] = new String[] {equipmenthost.toLowerCase(),equipmentbase.toLowerCase()};
        Log.i(TAG, "dbQueryOne: 查询是否存在某个货架");

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




    // 查询所有用户
    public String[] dbQueryAllEquipmengName() {
        ArrayList<String> equipmentArrayList = new ArrayList<String>();
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select equipmentname from t_equipment ";
        Cursor cursor = null;
        Log.i(TAG, "dbQueryAllEquipmengName: 查询所有的设备名称");
        try {
            cursor = cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                equipmentArrayList.add(cursor.getString(cursor.getColumnIndex("equipmentname")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        String [] s =new String[equipmentArrayList.size()];
        for(int i=0;i<equipmentArrayList.size();i++){
            s[i]=equipmentArrayList.get(i);
        }
        return s;
    }


    // 查询所有用户
    public ArrayList<Equipment> dbQueryAll() {
        ArrayList<Equipment> equipmentArrayList = new ArrayList<Equipment>();
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_equipment ";
        Cursor cursor = null;
        Log.i(TAG, "dbQueryAll: 查询所有设备");
        try {
            cursor  = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Equipment equipment = new Equipment();

                equipment.setEquipmentbase(cursor.getString(cursor.getColumnIndex("equipmentbase")));
                equipment.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                equipment.setEquipmentName(cursor.getString(cursor.getColumnIndex("equipmentname")));
                equipment.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));
                equipment.setEquipmentid(cursor.getString(cursor.getColumnIndex("equipmentid")));
                equipment.setId(cursor.getInt(cursor.getColumnIndex("id")));

                equipmentArrayList.add(equipment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        

        return equipmentArrayList;
    }



    // 删除用户，其实是把相应的isDel值从0改1        正在使用
    public void dbDeleteEquipment(String  equipment_base,String equipment_host) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete  from t_equipment where equipmentbase=? and equipmenthost =?";
        Object bindArgs[] = new Object[] { equipment_base.toLowerCase(),equipment_host.toLowerCase() };
        Log.i(TAG, "dbDeleteEquipment: 删除设备");
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


    // 删除设备  不是本机的设备。。。
    public void dbDeleteEquipmentNotEquipmenthost(String  equipment_host) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete  from t_equipment where equipmenthost !=?";
        Log.i(TAG, "dbDeleteEquipmentNotEquipmenthost: 删除主机不是现有主机的设备");
        Object bindArgs[] = new Object[] { equipment_host.toLowerCase() };
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


    // 删除设备    删除多余的数据
    public void dbDelete(String  equipment_host,String equipmentbase) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete  from t_equipment where equipmenthost =? and equipmentbase =?";
        Log.i(TAG, "dbDelete: 删除多余的数据");
        Object bindArgs[] = new Object[] { equipment_host.toLowerCase(),equipmentbase.toLowerCase() };
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


    // 删除所有产品
    public void dbDeleteAllEquipment(String equipmenthost ) {

        // sqliteDatabase = dbOpenHelper.getWritableDatabase();

        String sql = "delete from t_equipment where equipmentbase !=? ";


        Log.i(TAG,"删除所有设备："+sql);

        Object bindArgs[] = new Object[]{xmppConnect.getuserName().toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }


}
