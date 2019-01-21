package com.onesmock.activity.messNetXmppSerialport;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.advertisement.Advertisement;
import com.onesmock.dao.advertisement.AdvertisementDao;
import com.onesmock.dao.author.Author;
import com.onesmock.dao.author.AuthorDao;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.product.ProductDao;
import com.onesmock.dao.video.Video;
import com.onesmock.dao.video.VideoDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 主动请求的信息  作为基本信息 每次打开一个Activity都会请求
 */
public class NetMessFromPhp {

    private static final String TAG = "NetMessFromPhp";
    protected static SystemValuesDao systemValuesDao;
    protected static ProductDao productDao;
    protected static EquipmentDao equipmentDao;
    protected static AuthorDao authorDao;
    protected static AdvertisementDao advertisementDao;
    protected static VideoDao videoDao;


    protected static OkHttpClient okHttpClient;
    protected static Call call;
    protected static Request request;
    protected static FormBody formBody;
    protected static Response response0;
    protected static ResponseBody responseBody0;


    /**
     * 初始化变量使用
     *
     * @param context
     */
    public static void getInstance(Context context) {
        systemValuesDao = new SystemValuesDao(context);
        equipmentDao = new EquipmentDao(context);
        productDao = new ProductDao(context);
        authorDao = new AuthorDao(context);
        advertisementDao = new AdvertisementDao(context);
        videoDao = new VideoDao(context);


/*        //删除所有数据信息
        videoDao.dbDeleteAllVideo(xmppConnect.getuserName());


        advertisementDao.dbDeleteAllAdvertisement(xmppConnect.getuserName());
        equipmentDao.dbDeleteAllEquipment(xmppConnect.getuserName());
        productDao.dbDeleteAllProduct(xmppConnect.getuserName());
        authorDao.dbDeleteAllAuthor(xmppConnect.getuserName());


        */


    }



//   00 00 00 00 00 00 00 10
    //请求管理员信息
    public static void getJavaAll(Context context) {
        getInstance(context);

       // productDao.dbDeleteAllProduct();
      //  equipmentDao.dbDeleteAllEquipment();
        //advertisementDao.dbDeleteAllAdvertisement();
        // 请求管理员信息
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        formBody = new FormBody.Builder()
                .add(ConstantValue.equipment_host, systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getValue())
             //   .add(ConstantValue.platform, ConstantValue.phpAndroid)
             //   .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                .build();
        request = new Request.Builder()
                .url(/*ConstantValue.https+xmppConnect.getPHPIpAddress() + ConstantValue.phpgetGoodsImgUrl*/
             ConstantValue.https+ xmppConnect.getPHPIpAddress()+ConstantValue.javaMechineShelfList)//请求的url
                .post(formBody)
                .build();
        Log.i(TAG, "getJavaAll: "+  xmppConnect.getPHPIpAddress()+ConstantValue.javaMechineShelfList);
        //创建/Call
        call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new okhttp3.Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == ConstantValue.Okhttp_Back_Success) {


            /*        advertisementDao.dbDeleteAllAdvertisement(xmppConnect.getuserName());
                    equipmentDao.dbDeleteAllEquipment(xmppConnect.getuserName());
                    productDao.dbDeleteAllProduct(xmppConnect.getuserName());
                    authorDao.dbDeleteAllAuthor(xmppConnect.getuserName());
*/


                    ResponseBody responseBody = response.body();
                    String s = responseBody.string();
                    // System.out.println(response.body().string());
                    JSONObject myJsonObject = null;
                    JSONObject myJsonObject0 = null;
                    JSONArray jsonArray = null;
                   // advertisementDao.dbDeleteAllAdvertisement();
                    try {
                        myJsonObject = new JSONObject(s);


                        String code = myJsonObject.getString("code");
                        String msg = myJsonObject.getString("msg");
                        String data = myJsonObject.getString("data");
                        String count = myJsonObject.getString("count");



                        Log.i(TAG, "onResponse: java全部信息"+s);
                        Log.i(TAG, "onResponse: "+code+msg+count+"------"+data);
                    //    myJsonObject0 = new JSONObject(data);

                      //  Log.i(TAG, "onResponse: "+list.toString());
                        String list = myJsonObject.getString("data");
                        jsonArray = new JSONArray(list);
                        Log.i(TAG, "onResponse: "+jsonArray.length()+jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String equipmentId = jsonObject.getString("equipmentId");
                                String readyProductName = jsonObject.getString("readyProductName");
                                String nowProductName = jsonObject.getString("nowProductName");
                                String nowPhotoName = jsonObject.getString("nowPhotoName");
                                String readyPhotoName = jsonObject.getString("readyPhotoName");
                                String readyId = jsonObject.getString("readyId");
                                String shelfId = jsonObject.getString("shelfId");
                                String nowId = jsonObject.getString("nowId");
                                String assetId = jsonObject.getString("assetId");
                                //String readyProductName = jsonObject.getString("readyProductName");

                                ArrayList arrayList = equipmentDao.dbQueryAll();


                                int c = arrayList.size();


                                int a = equipmentDao.dbQueryEquipment(equipmentId.toLowerCase(), shelfId.toLowerCase());
                                int b = productDao.dbQueryOne(equipmentId.toLowerCase(), shelfId.toLowerCase());
                                if (a != 1 && b != 1) {
                                    equipmentDao.dbDelete(equipmentId.toLowerCase(),shelfId.toLowerCase());
                                    productDao.dbDelete(equipmentId.toLowerCase(),shelfId.toLowerCase());
                                    Log.i(TAG, "onResponse: 插入数据");
                                    equipmentDao.dbInsertEquipment("设备" + c, shelfId.toLowerCase(), equipmentId.toLowerCase());
                                    productDao.dbInsert(equipmentId.toLowerCase(), shelfId.toLowerCase());
                                }



                                if(null ==nowProductName || "".equals( nowProductName)){
                                    productDao.dbUpdateProductNow(shelfId, equipmentId, null, null);
                                    Log.i(TAG, "onResponse: 插入数据");
                                }else{
                                    productDao.dbUpdateProductNow(shelfId, equipmentId, nowPhotoName,nowProductName);
                                }

                            }


                                if (jsonArray.length() > 0) {

                                    ArrayList arrayList = equipmentDao.dbQueryAll();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String equipmentId = jsonObject.getString("equipmentId");
                                        String readyProductName = jsonObject.getString("readyProductName");
                                        String nowProductName = jsonObject.getString("nowProductName");
                                        String nowPhotoName = jsonObject.getString("nowPhotoName");
                                        String readyPhotoName = jsonObject.getString("readyPhotoName");
                                        String readyId = jsonObject.getString("readyId");
                                        String shelfId = jsonObject.getString("shelfId");
                                        String nowId = jsonObject.getString("nowId");
                                        String assetId = jsonObject.getString("assetId");
                                        //String readyProductName = jsonObject.getString("readyProductName");

                                        //预配产品信息
                                        int d = equipmentDao.dbQueryEquipment(equipmentId, shelfId);//预配信息更新
                                        ArrayList arrayList0 = equipmentDao.dbQueryAll();
                                        int e = productDao.dbQueryOne(equipmentId, shelfId);
                                        int f = arrayList.size() + 1;
                                        if (d > 0 && e > 0) {
                                            //  equipmentDao.dbInsertEquipment("设备"+c,shelf_code,equipment_code);
                                            Log.i(TAG, "onResponse: 插入预配信息");
                                            productDao.dbUpdateProductPrematch(readyPhotoName, readyProductName, shelfId, equipmentId);
                                        }

                                    }
                                } else {
                                  //  productDao.dbUpdateProductPrematchToZero();
                                }







                    } catch (Exception e) {
                    }

                }
            }
        });
    }




    //   00 00 00 00 00 00 00 10
    //请求管理员信息
    public static void getVideo(Context context) {
        getInstance(context);

        videoDao.dbDeleteAllVideo(xmppConnect.getuserName());
        // productDao.dbDeleteAllProduct();
        //  equipmentDao.dbDeleteAllEquipment();
        //advertisementDao.dbDeleteAllAdvertisement();
        // 请求管理员信息
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        formBody = new FormBody.Builder()
                .add(ConstantValue.equipment_host, systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getValue())
                //   .add(ConstantValue.platform, ConstantValue.phpAndroid)
                //   .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                .build();
        request = new Request.Builder()
                .url(/*ConstantValue.https+xmppConnect.getPHPIpAddress() + ConstantValue.phpgetGoodsImgUrl*/
                        ConstantValue.https+ xmppConnect.getPHPIpAddress()+ConstantValue.javaGetVideo)//请求的url
                .post(formBody)
                .build();
        Log.i(TAG, "------------------"+  xmppConnect.getPHPIpAddress()+ConstantValue.javaGetVideo);
        //创建/Call
        call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new okhttp3.Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == ConstantValue.Okhttp_Back_Success) {

                   // videoDao.dbDeleteAllVideo(xmppConnect.getuserName());

                    ResponseBody responseBody = response.body();
                    String s = responseBody.string();


                    // System.out.println(response.body().string());
                    JSONObject myJsonObject = null;
                    JSONObject myJsonObject0 = null;
                    JSONArray jsonArray = null;
                    // advertisementDao.dbDeleteAllAdvertisement();
                    try {
                        myJsonObject = new JSONObject(s);


                        String code = myJsonObject.getString("code");
                        String msg = myJsonObject.getString("msg");
                        String data = myJsonObject.getString("data");
                        String count = myJsonObject.getString("count");



                        Log.i(TAG, "onResponse: 视频全部信息"+s);
                        Log.i(TAG, "onResponse: "+code+msg+count+"------"+data);
                        //    myJsonObject0 = new JSONObject(data);

                        //  Log.i(TAG, "onResponse: "+list.toString());
                        String list = myJsonObject.getString("data");
                        jsonArray = new JSONArray(list);
                        Log.i(TAG, "onResponse: "+jsonArray.length()+jsonArray.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String adv_url = jsonObject.getString("videoName");
                           /* String readyProductName = jsonObject.getString("readyProductName");
                            String nowProductName = jsonObject.getString("nowProductName");
                            String nowPhotoName = jsonObject.getString("nowPhotoName");
                            String readyPhotoName = jsonObject.getString("readyPhotoName");
                            String readyId = jsonObject.getString("readyId");
                            String shelfId = jsonObject.getString("shelfId");
                            String nowId = jsonObject.getString("nowId");
                            String assetId = jsonObject.getString("assetId");
                            //String readyProductName = jsonObject.getString("readyProductName");*/
                            Video video = null;
                           video=videoDao.dbQueryOne(adv_url);
                           if(null==video){
                               videoDao.dbInsertVideo(adv_url,xmppConnect.getuserName());
                           }


                        }

                    } catch (Exception e) {
                    }

                }
            }
        });
    }



}
