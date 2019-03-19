package com.onesmock.Util.DBUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

import android_serialport_api.SerialPortFinder;


/**
 *
 * 数据库信息
 */

public class   DBOpenHelper extends SQLiteOpenHelper {
    private String TAG = "数据库初始信息0：";
    private static SQLiteDatabase sqliteDatabase = null;

    private SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private static DBOpenHelper dbOpenHelper = null;
    private static SharedPreferences sp ;
    private static String DBname= "njlsj.db";

    private static final int DATABASE_VERSION = 2;//用来测试的时候的数据库版本
    //大部分版本号为 2
    //初始数据库信息为1
    //数据库版本参数不正确  会导致全部数据信息清除


    public synchronized static DBOpenHelper getInstance(Context context) {//获取DBOpenHelper，没有就生成，有直接返回
        if (dbOpenHelper == null) {
            dbOpenHelper = new DBOpenHelper(context,DBname, null, 1);
        }/**
         myDatabase =SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.NO_LOCALIZED_COLLATORS |SQLiteDatabase.OPEN_READWRITE);
         */

        return dbOpenHelper;
    };



    public synchronized static SQLiteDatabase getInstanceSQLiteDatabase() {
        if (sqliteDatabase == null) {//获取sqliteDabase   没有 重新生成，有直接返回
            sqliteDatabase = dbOpenHelper.getWritableDatabase();
        }
        return sqliteDatabase;
    };

    private static boolean mainTmpDirSet = false;
    @Override
    public SQLiteDatabase getReadableDatabase() {
        if (!mainTmpDirSet) {//设置临时文件  存放数据库信息
            boolean rs = new File("/data/data/com.onesmock/databases/main").mkdir();
            Log.d("ahang", rs + "");
            super.getReadableDatabase().execSQL("PRAGMA temp_store_directory = '/data/data/com.onesmock/databases/main'");
            mainTmpDirSet = true;
            return super.getReadableDatabase();
        }
        return super.getReadableDatabase();
    }


    public DBOpenHelper(Context context, String name, CursorFactory factory,
                        int version) {//构造函数

        super(context, "njlsj.db", null, DATABASE_VERSION);
        String databaseName = getDatabaseName();//数据库名字
        Log.i(TAG, "创建数据库1");
        //向系统申请一个SqliteTest.db文件存这个数据库，其中1是数据库版本。
    }


   private final String t_advertisement =
            "create table if not exists t_advertisement (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "goods_img_id VARCHAR(255)," +
                    "adv_url VARCHAR(255)" +
                    ")";

    private final String t_video =
            "create table if not exists t_video (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "equipmenthost VARCHAR(255)," +
                    "adv_url VARCHAR(255)" +
                    ")";
    private final String t_author =
            "create table if not exists t_author(" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "author_name VARCHAR(255)," +
                    "author_phone VARCHAR(255)," +
                    "equipmenthost VARCHAR(255)NOT NULL," +
                    "CreatedTime Long " +//CURRENT_TIMESTAMP
                    ")";

    private final String t_product =
            "create table if not exists t_product(" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "product_name VARCHAR(255)," +
                    "productDaysum VARCHAR(255)," +
                    "productTotal VARCHAR(255)," +
                    "productId INTEGER," +
                    "CreatedTime Long ," +//CURRENT_TIMESTAMP
                    "imgUrl VARCHAR(255)," +
                    "equipmenthost VARCHAR(255) NOT NULL," +
                    "equipmentbase VARCHAR(255) NOT NULL," +
                    "prematchImgurl VARCHAR(255)," +
                    "prematchProductname VARCHAR(255)," +
                    "productMess VARCHAR(255)" +// 产品介绍的文字信息

                    ")";

    private final String t_systemvalues =
            "create table if not exists t_systemvalues(" +
                    "id INTEGER NOT NULL PRIMARY KEY  ," +
                    "name VARCHAR(255)," +
                    "value VARCHAR(255)," +
                    "equipmenthost VARCHAR(255) NOT NULL," +
                    "CreatedTime Long  " +//CURRENT_TIMESTAMP
                    ")";

    private final String t_equipment =
            "create table if not exists t_equipment(" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "equipmentname VARCHAR(255)," +
                    "equipmentbase VARCHAR(255)NOT NULL," +
                    "equipmenthost VARCHAR(255)NOT NULL," +
                    "equipmentid VARCHAR(255) ," +
                    "CreatedTime Long  " +//CURRENT_TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
                    ")";


    private final String t_productmess =
            "create table if not exists t_productmess (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "equipmentbase VARCHAR(255) NOT NULL," +
                    "imgUrl VARCHAR(255)," +
                    "videoUrl VARCHAR(255)," +
                    "CreatedTime Long  " +//CURRENT_TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
                    ")";
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {

        final int FIRST_DATABASE_VERSION = 1;//     第一版本数据库版本号   对应java版本还没有出货，需要重新修改

    /*    String t_advertisement = "create table if not exists t_advertisement(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "equipment VARCHAR(255)," +
                "CreatedTime Long  " +//CURRENT_TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
                ")";*/
        //如果初次运行，建立一张t_user表，建表的时候注意，自增是AUTOINCREMENT，而不是mysql的AUTO_INCREMENT
        boolean isTableExist = true;


        String sql0 = "select count(*) as c from Sqlite_master  where type = 'table'  and  name =  '`' ";
        Cursor cursor = null;
        int a = 0;
        try {
            cursor = sqliteDatabase.rawQuery(sql0, null);


            if (cursor.moveToNext()) {//查看是否拥有数据，新的设置，如果有，就不再重新建立数据库，否则创建数据库
                int count = cursor.getInt(0);
                if (count > 0) {
                    isTableExist = false;
                }
            }


            if (cursor.moveToNext()) {// 判断Cursor中是否有数据
                a = cursor.getInt(0);// 返回总记录数
            } else {
                a = 0;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

/*
        tabbleIsExist("t_systemvalues");//判断数据表是否存在
        if(tabbleIsExist("t_systemvalues")){//系统数据
            Log.i(TAG, "onCreate: 已经存在");
        }else{
            sqliteDatabase.execSQL(t_systemvalues);
            Log.i(TAG , t_systemvalues);
        }


        if(tabbleIsExist("t_video")){//视频数据
            Log.i(TAG, "onCreate: 已经存在");

        }else{
            sqliteDatabase.execSQL(t_video);
            Log.i(TAG , t_video);
        }

        if(tabbleIsExist("t_equipment")){//设备数据
            Log.i(TAG, "onCreate: 已经存在");

        }else{
            sqliteDatabase.execSQL(t_equipment);
            Log.i(TAG, t_equipment);
        }

        if(tabbleIsExist("t_product")){//产品数据
            Log.i(TAG, "onCreate: 已经存在");

        }else{
            sqliteDatabase.execSQL(t_product);
            Log.i(TAG, t_product);
        }
        if(tabbleIsExist("t_author")){//管理员数据
            Log.i(TAG, "onCreate: 已经存在");

        }else{
            sqliteDatabase.execSQL(t_author);
            Log.i(TAG, t_author);
        }
        if(tabbleIsExist("t_advertisement")){//设备数据
            Log.i(TAG, "onCreate: 已经存在");

        }else{
            sqliteDatabase.execSQL(t_advertisement);
            Log.i(TAG, t_advertisement);
        }*/

        sqliteDatabase.execSQL(t_systemvalues);
        sqliteDatabase.execSQL(t_video);
        sqliteDatabase.execSQL(t_equipment);
       // sqliteDatabase.execSQL(t_product);
        sqliteDatabase.execSQL(t_author);
        sqliteDatabase.execSQL(t_advertisement);

        sqliteDatabase.execSQL(t_video);//升级数据库，建立新的表信息
        sqliteDatabase.execSQL(t_productmess);//产品的详情信息
        sqliteDatabase.execSQL(t_product);//产品的信息

    /*           if (isTableExist) {
            Log.i(TAG, "onCreate: 创建表格");
            sqliteDatabase.execSQL(t_advertisement);
            Log.i(TAG, t_advertisement);
            sqliteDatabase.execSQL(t_equipment);
            Log.i(TAG, t_equipment);
            sqliteDatabase.execSQL(t_product);
            Log.i(TAG, t_product);
            sqliteDatabase.execSQL(t_systemvalues);
            Log.i(TAG , t_systemvalues);
            sqliteDatabase.execSQL(t_author);
            Log.i(TAG , t_author);
            sqliteDatabase.execSQL(t_video);
            Log.i(TAG , t_video);


        }
*/

        if (0 == a) {

            sqliteDatabase.execSQL("insert into t_equipment (equipmentid,equipmenthost,equipmentbase,equipmentname,CreatedTime)values ('0','020000000000000001','020000000000000001','主设备','')");

         /*   sqliteDatabase.execSQL("insert into t_equipment (equipmenthost,equipmentbase,equipmentname,CreatedTime)values ('010100000001','020200000001','设备0','')");
            sqliteDatabase.execSQL("insert into t_equipment (equipmenthost,equipmentbase,equipmentname,CreatedTime)values ('010100000001','020200000010','设备1','')");
          */


            //product_total ,product_daysum,    0,0,
            //sqliteDatabase.execSQL("insert into t_product (productId,equipmenthost,equipmentbase,imgUrl,productTotal ,productDaysum,product_name,CreatedTime)values (1,'0000000000000001','0000000000000001', '', '0','0','主设备','')");
           /* sqliteDatabase.execSQL("insert into t_product (productId,equipmenthost,equipmentbase,imgUrl,product_total ,product_daysum,product_name,CreatedTime)values (1,'010100000001','020200000001', 'http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg', 0,0,'红河','')");

*/
              Log.i(TAG + "插入数据1", "");


            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('初始密码','666666','020000000000000001','')");
            Log.i(TAG + "插入数据2", "");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('本机编号','020000000000000001','020000000000000001','')");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('超级密码','888888','020000000000000001','')");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('网络地址','www.xxxx.com','020000000000000001','')");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('wifi','wifi设置','020000000000000001','')");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values ('音量控制','音量控制','020000000000000001','')");

            Log.i(TAG + "插入数据3", "");
           // sqliteDatabase.execSQL("insert into t_author (author_name,author_phone,equipmenthost,CreatedTime)values ('张三', '12345678901', '0000000000000001','')");
            Log.i(TAG + "插入数据4", "");
            // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
            //Object bindArgs[] = new Object[] { uuid };
            // 执行这条无返回值的sql语句
            // sqliteDatabase.execSQL(t_equipment_sql, bindArgs);

            // String[] items = 7uj

            String sql=("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values (?,?,?,?)");
            sqliteDatabase.execSQL("insert into t_systemvalues(name,value,equipmenthost,CreatedTime) values  ('串口号码', '/dev/ttyS3', '020000000000000001', '')");
            sqliteDatabase.execSQL("insert into t_systemvalues(id,name,value,equipmenthost,CreatedTime) values  (0,'wifi', '00000000', '020000000000000001', '')");

            //保存串口号码为固定数值  禁止修改

            Log.i(TAG + "串口号码5","");


        }

        onUpgrade(sqliteDatabase, FIRST_DATABASE_VERSION, DATABASE_VERSION);//数据库升级信息   初始版本，现在版本，sql语句信息
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//数据库升级的方法 在这里进行操作
        //这里是更新数据库版本时所触发的方法
       // db.execSQL("drop table if exists Book");
       // db.execSQL("drop table if exists Category");    // 增加一个表,创建表语句.
      //  onCreate(db);//

    }



    public boolean tabbleIsExist(String tableName) {//判断数据表是否存在
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from " + DBname+ " where type ='table' and name ='" + tableName + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;//存在
                }
            }

        } catch (Exception e) {
            // TODO: handle exception

        }/*finally {
            cursor.close();
        }*/

        return result;
    }





}