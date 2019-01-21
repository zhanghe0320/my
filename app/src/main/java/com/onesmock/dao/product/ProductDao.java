package com.onesmock.dao.product;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onesmock.Util.DBUtil.DBOpenHelper;
import com.onesmock.dao.equipment.Equipment;

import java.util.ArrayList;

public class ProductDao {


  //  private SQLiteDatabase sqliteDatabase;// 创建SQLiteDatabase对象

    private DBOpenHelper dbOpenHelper;
    private static final String TAG = "ProductDao";

    public ProductDao(Context context)// 定义构造函数
    {
        dbOpenHelper =DBOpenHelper.getInstance(context);// 初始化DBOpenHelper对象
        //sqliteDatabase= DBOpenHelper.getInstanceSQLiteDatabase(context);
    }



    // 插入产品数据   正在使用中
    public void dbInsert(String equipmenthost, String equipmentbase) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        String sql = "insert into t_product(equipmenthost,equipmentbase) values (?,?)";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] { equipmenthost.toLowerCase(), equipmentbase.toLowerCase() };
        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }


    // 插入产品数据   正在使用中
    public void dbUpdateOldToNew(String productName,String equipmenthost, String equipmentbase) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        Long time=  System.currentTimeMillis();//CreatedTime =?

        String sql = "update  t_product set equipmenthost=? ,equipmentbase =?,CreatedTime =?  where product_name =?";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] { equipmenthost.toLowerCase(), equipmentbase.toLowerCase(),time,productName };
        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }



    // 插入产品数据
    public void dbInsertProduct( String equipmenthost,String equipmentbase) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();// 以读写方法打开数据库，不仅仅是写，getReadableDatabase()是只读
        String sql = "insert into t_product (equipmenthost,equipmentbase)values ( ?,?)";
        // 传递过来的username与password分别按顺序替换上面sql语句的两个?，自动转换类型，下同，不再赘述
        Object bindArgs[] = new Object[] { equipmenthost.toLowerCase(), equipmentbase.toLowerCase()};
        // 执行这条无返回值的sql语句
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }
    // 求出表中有多少条数据
    public int dbGetProductSize() {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select count(*) from t_product";
        Cursor cursor = null;

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


    // 修改 使用中
    public void dbUpdateProduct(String  equipmentbase,String  equipmenthost,int productid) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        Long time=  System.currentTimeMillis();//CreatedTime =?
        String sql = "update t_product set equipmentbase=?,CreatedTime =? where productid=? and equipmenthost =?";

        Log.i(TAG,"修改基础的设备号码："+sql+"产品名字"+productid+"设备名字"+equipmentbase+"时间");
        Object bindArgs[] = new Object[] { equipmentbase.toLowerCase(),time,productid,equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }

    // 修改 现在拥有的产品信息
    public void dbUpdateProductNow(String  equipmentbase,String  equipmenthost,String  imgUrl,String product_name ) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        Long time=  System.currentTimeMillis();
        String sql = "update t_product set imgUrl=? ,product_name=? where equipmentbase=? and equipmenthost =?";

        Log.i(TAG,"现有产品的修改："+sql+"产品名字"+imgUrl+"设备名字"+product_name+"时间");
        Object bindArgs[] = new Object[] { imgUrl,product_name,equipmentbase.toLowerCase(),equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }

    // 修改        使用中
    public void dbUpdateProductPrematch(String prematchImgurl,String prematchProductname,String  equipmentbase,String  equipmenthost) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "update t_product set prematchImgurl=?,prematchProductname=?where equipmentbase=? and equipmenthost =?";
      /*,product_name='', imgUrl=''*/
        Log.i(TAG,"预配信息："+sql+"产品名字"+prematchImgurl+"设备名字"+prematchProductname+"时间");
        Object bindArgs[] = new Object[] { prematchImgurl,prematchProductname,equipmentbase.toLowerCase(),equipmenthost.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }

    // 修改        使用中
    public void dbUpdateProductPrematchToZero() {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "update t_product set prematchImgurl='',prematchProductname='' ";
        /*,product_name='', imgUrl=''*/
        Log.i(TAG,"将预配信息修改为现有信息："+sql);
        Object bindArgs[] = new Object[] { };
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }




    // 修改 使用中
    public int dbQueryOne(String  equipmenthost,String  equipmentbase) {
     //   sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "select count(*) from t_product where equipmentbase=? and equipmenthost =?";

        Log.i(TAG,"查询是否存在某产品："+sql);

        String bindArgs[] = new String[] { equipmentbase.toLowerCase(),equipmenthost.toLowerCase()};

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



    // 修改 使用中  修改所有的设备的主机设备信息
    public void dbDeleteProductEquipmenthost(String  equipmenthostold) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "delete from t_product  where equipmenthost !=? ";

        Log.i(TAG,"将设备号修改新的设备号："+sql);
        Object bindArgs[] = new Object[] { equipmenthostold};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }


    // 修改 使用中  修改所有的设备的主机设备信息
    public void dbDelete(String  equipmenthostold,String equipmentbase) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "delete from t_product  where equipmenthost =?  and equipmentbase =?";

        Log.i(TAG, "dbDelete: 删除多余的数据！");
        Object bindArgs[] = new Object[] { equipmenthostold.toLowerCase(),equipmentbase.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }


    // 修改     使用中
    public void dbUpdateProductTotal(String  equipmentbase,String productTotal,String productDaysum ) {
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        //String sql = "update t_product set product_name= ?,CreatedTime=? where product_EquipmentName=?";
        String sql = "update t_product set productTotal=?,productDaysum=? where equipmentbase=?";

        Log.i(TAG,"修改产品的出货量："+sql);
        Object bindArgs[] = new Object[] { productTotal,productDaysum,equipmentbase.toLowerCase()};
        dbOpenHelper.getWritableDatabase().execSQL(sql,bindArgs);

    }


    // 查询所有的产品
    public ArrayList<Product> dbQueryAll() {
        ArrayList<Product> productArrayList = new ArrayList<Product>();
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_product";

        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex("id")));
                product.setEquipmentbase(cursor.getString(cursor.getColumnIndex("equipmentbase")));
                product.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                product.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
                product.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                product.setProductTotal(cursor.getString(cursor.getColumnIndex("productTotal")));
                product.setProductDaysum(cursor.getString(cursor.getColumnIndex("productDaysum")));
                product.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                product.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));

                product.setPrematchImgurl(cursor.getString(cursor.getColumnIndex("prematchImgurl")));
                product.setPrematchProductname(cursor.getString(cursor.getColumnIndex("prematchProductname")));
                productArrayList.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.i(TAG,"查询所有的产品："+sql);


        return productArrayList;
    }


    // 查询所有的产品   使用中  查询所有图片
    public ArrayList dbQueryAllImgurl() {
        ArrayList<String> Imgurl = new ArrayList<String>();
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_product   where imgUrl !='' ";

        String bindArgs[] = new String[] { null};

        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql,null );

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Imgurl.add(cursor.getString(cursor.getColumnIndex("imgUrl")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG,"查询所有图片："+sql);


      /*  String [] s =new String[Imgurl.size()];
        for(int i=0;i<Imgurl.size();i++){
            s[i]=Imgurl.get(i);
        }*/
        return Imgurl;
    }

    // 查询所有的产品  使用中  查询所有产品名字
    public ArrayList dbQueryAllProductname() {
        ArrayList<String> names = new ArrayList<String>();
      //  sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_product   where imgUrl !=''";
        String bindArgs[] = new String[] { null};


        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, null);

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

                names.add(cursor.getString(cursor.getColumnIndex("product_name")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG,"查询所有产品名字："+sql);


     /*   String [] s =new String[names.size()];
        for(int i=0;i<names.size();i++){
            s[i]=names.get(i);
        }*/
        return names;
    }


    // 根据ID删除数据
    public void dbDeleteProduct(String   equipmenthost,String equipmentbase) {


       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete  from t_product where equipmenthost=? and equipmentbase =?";
        Object bindArgs[] = new Object[] { equipmenthost.toLowerCase(),equipmentbase.toLowerCase() };
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);


    }

    // 根据ID删除数据
    public void dbDeleteProductNotEquipmenthost(String   equipment_host) {


       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "delete  from t_product where equipmenthost !=?";
        Object bindArgs[] = new Object[] { equipment_host };
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);


    }



    // 根据设备号码查询产品  使用中
    public Product dbQueryOneProduct(String equipmentbase) {
       // sqliteDatabase = dbOpenHelper.getWritableDatabase();
        String sql = "select * from t_product where equipmentbase=?";
        String[] selectionArgs = new String[] { equipmentbase };
        Log.i(TAG, "dbQueryOneProduct: "+equipmentbase);
        Cursor cursor = null;

        try {
            cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, selectionArgs);

            // 游标从头读到尾
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex("id")));
                product.setEquipmentbase(cursor.getString(cursor.getColumnIndex("equipmentbase")));
                product.setEquipmenthost(cursor.getString(cursor.getColumnIndex("equipmenthost")));
                product.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
                product.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                product.setProductTotal(cursor.getString(cursor.getColumnIndex("productTotal")));
                product.setProductDaysum(cursor.getString(cursor.getColumnIndex("productDaysum")));
                product.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                product.setCreatedTime(cursor.getLong(cursor.getColumnIndex("CreatedTime")));

                product.setPrematchImgurl(cursor.getString(cursor.getColumnIndex("prematchImgurl")));
                product.setPrematchProductname(cursor.getString(cursor.getColumnIndex("prematchProductname")));
                return  product;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }



        Log.i(TAG,"根据设备号码查询产品："+sql);


        return  null;
    }




    // 更新产品的类型  使用中的
    public void dbUpdateProductName(String product_name,String equipmentbase,String imgUrl) {

       // sqliteDatabase = dbOpenHelper.getWritableDatabase();

        String sql = "update t_product set  product_name=?,CreatedTime=? ,imgUrl=?,prematchImgurl='',prematchProductname='' where equipmentbase=?";
        Long time=  System.currentTimeMillis();

        Object bindArgs[] = new Object[] { product_name ,time,imgUrl,equipmentbase.toLowerCase()};

        Log.i(TAG,"更新产品的类型："+sql);

        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }


    // 更新产品的类型  使用中的
    public void dbUpdateProductAddTime(String equipmentbase ) {

       // sqliteDatabase = dbOpenHelper.getWritableDatabase();

        String sql = "update t_product set  CreatedTime=?  where equipmentbase=?";
        Long time=  System.currentTimeMillis();

        Object bindArgs[] = new Object[] {time,equipmentbase.toLowerCase()};

        Log.i(TAG,"更新产品的类型："+sql);

        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);

    }

    // 删除所有产品
    public void dbDeleteAllProduct(String equipmenthost ) {

        // sqliteDatabase = dbOpenHelper.getWritableDatabase();

        String sql = "delete from t_product where equipmenthost != ?";

        Log.i(TAG,"删除所有产品："+sql);

        Object bindArgs[] = new Object[]{equipmenthost};
        dbOpenHelper.getWritableDatabase().execSQL(sql, bindArgs);
    }



}
