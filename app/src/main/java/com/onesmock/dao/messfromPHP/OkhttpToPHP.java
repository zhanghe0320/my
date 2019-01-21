package com.onesmock.dao.messfromPHP;

import android.util.Log;

import com.onesmock.Util.DBUtil.ConstantValue;

import org.json.JSONArray;
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
public class OkhttpToPHP {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OkhttpToPHP{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }





    /**
     * 网络连接 okhttp相关
     *
     * @param savedInstanceState
     */
    protected OkHttpClient okHttpClient;
    protected FormBody formBody;
    protected Call call;
    protected Request request;

    /**
     * 相应请求信息
     */
    public String  sendMessToPHP(MessFromBase fromBase){

            while(fromBase.getMessageMachineSendToHeader().equals(ConstantValue.messageMachineSendToHeader)){

            }
        return "";
    }


    /**
     * 处理信息  发送到php平添回复的接口使用 调用
     */

    private static OkhttpToPHP toPHP = null;


    public OkhttpToPHP sendMess(MessFromBase fromBase){

        okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        formBody = new FormBody.Builder()
                .add(ConstantValue.equipment_host, fromBase.getEquipment_host())
                .add(ConstantValue.equipment_base,fromBase.getEquipment_base())
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

                    }catch (Exception e){

                    }

                    Log.i("okhttp里面的responseBody",s);
                }
                Log.i("okhttp里面responseBody",response.body().toString());

            }
        });
        return toPHP;
    }



    public String  returnCommandAndAddress(MessFromBase fromBase){





        return "";
    };





}
