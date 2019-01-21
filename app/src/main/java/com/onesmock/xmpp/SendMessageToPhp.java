package com.onesmock.xmpp;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.product.Product;
import com.onesmock.dao.product.ProductDao;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;


public class SendMessageToPhp  {


    //发送消息到设备
    public static String  SendMessToEquipment(Context context){
        SystemValuesDao systemValuesDao = new SystemValuesDao(context);
        String a = systemValuesDao.dbQueryOneByName(ConstantValue.serialPort).getValue();

        System.out.println(a+"sssssssssssssssssssssssss");

        return  "a";
    }


    //发送消息到PHP
    public static String  SendMessToPhp(Context context,String mess){
        OkHttpClient okHttpClient;
        Call call;
        Request request;
        FormBody formBody;
        Response response0;
        ResponseBody responseBody0;
        View view = new View(context);
        Toast toast;
        toast =new Toast(context);


        String m1 = mess.substring(0, 2);//消息头

        String m2 = mess.substring(2, 4);//消息内容

        String m3 = mess.substring(4, 6);//消息长度

        String m4 = mess.substring(6, 18);//消息接收地址

        String m5 = mess.substring(18, 30);//消息发送地址

        String m6 = mess.substring(30, mess.length());//消息发送地址

        Log.i("先试一下",mess);
        System.out.println(mess+"               1111111111111111111"+"消息头:"+ m1+"   "+"消息:"+ m2+"长度:"+ m3+"接收地址:"+ m4+"发送地址:"+m5+"     总长度"+mess.length());


        Log.i("消息111", mess.toString());
        System.out.println("111");

        boolean b = true;

        //串口信息有返回  则是出货成功，进行计数  并上传数据到平台
        Long time = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("equipment_host",m4);
            jsonObject.put("equipment_base",m5);
            jsonObject.put("mess",ConstantValue.Shipments);
        }catch (JSONException e){

        }

        /**
         * 向平台回复消息
         * 出货成功
         */

        okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        formBody = new FormBody.Builder()
                .add(ConstantValue.equipment_host, m4)
                .add(ConstantValue.equipment_base,m5)
                .add(ConstantValue.is_success,ConstantValue.phpIs_successYes)
                .add(ConstantValue.platform,ConstantValue.phpAndroid)
                .add(ConstantValue.phpAndroid,ConstantValue.phpVersion)
                .build();

        request = new Request.Builder()
                .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.phpAcceptDispatchUrl)//请求的url
                .post(formBody)
                .build();


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


                if(response.code()==ConstantValue.Okhttp_Back_Success) {
                    ResponseBody responseBody = response.body();
                    String s = responseBody.string();

                    // System.out.println(response.body().string());
                    JSONObject myJsonObject= null;
                    JSONObject myJsonObject0= null;
                    JSONArray jsonArray;
                    try{
                        myJsonObject= new JSONObject(s);

                        String code = myJsonObject.getString("code");
                        String msg = myJsonObject.getString("msg");
                        int c =Integer.parseInt(code);


                    }catch (Exception e){

                    }


                    Log.i("okhttp里面的responseBody",s);


                    Log.i("okhttp里面responseBody",s+"aaaaaaaaaaaaaaaaaaaaaaaa");
                }
                Log.i("okhttp里面responseBody",response.body().toString());



            }
        });


        return  "";
    }
}
