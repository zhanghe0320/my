package com.onesmock.activity.main.administratorInformation.operationManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.product.Product;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;



/**
 *  货架的信息的更新处理
 *  删除货架
 *  operationManagement
 */
public class operationManagementActivity  extends BaseActivityO {


    private static final String TAG = "operationManagementActi";
    //线性布局
    private LinearLayout operationManagement;
    //返回按钮
    public enum KeyID{btn_pedOK,btn_pwdNO};
    private AlertDialog dialog;

    static  ArrayList<Product> productList ;//查出表中的所有用户放到一个ArrayList中

    public void init(){
        // 注册组件
        operationManagement = (LinearLayout) findViewById(R.id.operationManagement);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operationmanagement);

        NetMessFromPhp.getJavaAll(context);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, operationManagementActivity.this);

        /**
         * 请求产品的预配信息
         */
        
        String host = systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getValue();
        init();
        SystemValues pass=  systemValuesDao.dbQueryOneByName(ConstantValue.localNumber);


        //求出用户的数量，并且显示
        int equipmentSize = equipmentDao.dbGetEquipmentSize(); //一定有设备


        //添加的textView1放到activity_table.xml中tag=1的View那个位置
        //如果用户数量为0就不用搞这么东西添加表格布局了

            ArrayList<Equipment> equipmentList = equipmentDao.dbQueryAll();//查出表中的所有用户放到一个ArrayList中

             productList = productDao.dbQueryAll();

            TableLayout tableLayout1 = new TableLayout(this);//新建一个表格布局
            tableLayout1.setStretchAllColumns(true);//自动宽度，使表格在横向占据100%
            //打印表头
            TableRow tableRow = new TableRow(this);//新建一行

            TextView textView = new TextView(this);//新建一个TextView
            textView.setTextSize(25);//设置字体
            textView.setText("编号");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);//放到行中，自动增加一个单元格

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("现有产品");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);


            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("待更新");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("操作");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("操作");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);


            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("操作");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);


            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("操作");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);


            tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            /////单独放置设置信息本及设备号码
            EditText editText;

            //打印用户信息
            for (int i = 0; i < productList.size(); i++) {

                Product product = productList.get(i);
                // 一个用户占据一行
                tableRow = new TableRow(this);

                textView = new TextView(this);
                textView.setTextSize(25);//货架名称
                textView.setText(String.valueOf(i+1));
                tableRow.addView(textView);
                textView.setGravity(Gravity.CENTER);


                    textView = new TextView(this);
                    textView.setTextSize(25);//货架产品
                    textView.setText(product.getProductName());
                    tableRow.addView(textView);
                    textView.setGravity(Gravity.CENTER);




                    textView = new TextView(this);
                    textView.setTextSize(25);//平台更新
                    textView.setText(productList.get(i).getPrematchProductname());
                    tableRow.addView(textView);
                    textView.setGravity(Gravity.CENTER);


                Button buttonOpenDoor = new Button(this);
                buttonOpenDoor.setText("开门");
                //imageview.setBackgroundColor(Color.parseColor("#f34649"));
                //
                //imageview.setBackgroundColor(Color.rgb(213, 0, 0));
                buttonOpenDoor.setTextSize(25);//操作
                buttonOpenDoor.setBackgroundColor(Color.parseColor("#90EE90"));
                textView.setGravity(Gravity.CENTER);


                Button buttonCloseDoor = new Button(this);
                buttonCloseDoor.setText("关门");
                buttonCloseDoor.setTextSize(25);//操作
                buttonCloseDoor.setBackgroundColor(Color.parseColor("#87CEFA"));
                textView.setGravity(Gravity.CENTER);


                Button buttonToNew = new Button(this);
                buttonToNew.setText("更换");
                buttonToNew.setTextSize(25);//操作
                buttonToNew.setBackgroundColor(Color.parseColor("#ffff7D3D"));
                textView.setGravity(Gravity.CENTER);



                Button buttonAddProduct = new Button(this);
                  /*  if(equipment.getEquipmentName().equals(ConstantValue.mainEquipment)){
                        buttonAddProduct.setEnabled(false);
                    }else {

                    }*/
                buttonAddProduct.setText("添加");
                buttonAddProduct.setTextSize(25);//操作
                // buttonAddProduct.setTextColor();
                buttonAddProduct.setBackgroundColor(Color.parseColor("#00FFFF"));
                textView.setGravity(Gravity.CENTER);



                buttonOpenDoor.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //这样可以获取按钮的id
                        //修改密码，更新数据库之后，刷新一下这个TableActivity

                        StringBuffer mess0=new StringBuffer();

                        String s = "";
                        mess0 = new StringBuffer();
                        mess0.append(ConstantValue.messageHeaderSendToMachine);//55  消息头
                        mess0.append(ConstantValue.openTheDoor);//01  消息//01  消息
                        mess0.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据


                        Long longNum = Long.parseLong( product.getEquipmentbase());
                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                        byte[] long2Bytes = byteUtil.long2Bytes(longNum);
                        byteUtil.BinaryToHexString(long2Bytes);
                        mess0.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

                        longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmenthost());
                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                        long2Bytes = byteUtil.long2Bytes(longNum);
                        byteUtil.BinaryToHexString(long2Bytes);
                        mess0.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001



                       // mess0.append();//01 01 00000000
                      //  mess0.append();//02 01 00000000                mess0.append(valueMess.checkoutBit);





                        mess0.append(ConstantValue.checkoutBit);//校验位

                       /* Message  message = Message.obtain();
                        message.obj= mess0.toString();
                        xmppConnect.messHandlerSendMain.sendMessage(message);*/
                        byte[] bytes0= byteUtil.hexStringToByteArray(mess0.toString());
                        Log.i(TAG, "handleMessage: "+mess0.toString()+"+++++++++");
                        BaseActivity.SendMessage(bytes0);

                        showToastView("正在打开["+product.getProductName()+"]的舱门，请稍候！");
                        openActivity(operationManagementActivity.class);
                        AppManager.getInstance().killActivity(operationManagementActivity.class);


                    }
                });

                buttonCloseDoor.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //这样可以获取按钮的id
                        //修改密码，更新数据库之后，刷新一下这个TableActivity

                        StringBuffer mess0=new StringBuffer();

                        String s = "";
                        mess0 = new StringBuffer();
                        mess0.append(ConstantValue.messageHeaderSendToMachine);//55  消息头
                        mess0.append(ConstantValue.closeTheDoor);//01  消息//01  消息
                        mess0.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据

                        Long longNum = Long.parseLong( product.getEquipmentbase());
                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                        byte[] long2Bytes = byteUtil.long2Bytes(longNum);
                        byteUtil.BinaryToHexString(long2Bytes);
                        mess0.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

                        longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmenthost());
                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                        long2Bytes = byteUtil.long2Bytes(longNum);
                        byteUtil.BinaryToHexString(long2Bytes);
                        mess0.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001



                        // mess0.append(product.getEquipmentbase());//01 01 00000000
                      //  mess0.append(equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmenthost());//02 01 00000000                mess0.append(valueMess.checkoutBit);
                        mess0.append(ConstantValue.checkoutBit);//校验位

                       /* Message  message = Message.obtain();
                        message.obj= mess0.toString();
                        xmppConnect.messHandlerSendMain.sendMessage(message);*/
                        byte[] bytes0= byteUtil.hexStringToByteArray(mess0.toString());
                        Log.i(TAG, "handleMessage: "+mess0.toString()+"+++++++++");
                        BaseActivity.SendMessage(bytes0);

                        showToastView("正在关闭["+product.getProductName()+"]的舱门，请稍候！");
                        openActivity(operationManagementActivity.class);
                        AppManager.getInstance().killActivity(operationManagementActivity.class);


                    }
                });

                buttonAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        product.getPrematchProductname().equals(product.getProductName());
                        AlertDialog MyDialog = new AlertDialog.Builder(operationManagementActivity.this).create();//创建对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(operationManagementActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                        builder.setTitle("更新产品信息");
                        builder.setMessage("确定更新设备产品吗?修改后不可恢复！");

                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // int product_total= 1000+product.getProductDaysum();
                                Log.i("设备", product.getEquipmentbase());

                           /*     if(product.getProductName().equals(product.getPrematchProductname())&&product.getImgUrl().equals(product.getPrematchImgurl())){

                                }else{*/

                                okHttpClient = new OkHttpClient.Builder()
                                        .connectTimeout(10, TimeUnit.SECONDS)
                                        .writeTimeout(10, TimeUnit.SECONDS)
                                        .readTimeout(20, TimeUnit.SECONDS)
                                        .build();
                                Log.i(TAG, "onClick: "+BaseActivity.bundle.getString(ConstantValue.employeeId));
                                //post方式提交的数据
                                formBody = new FormBody.Builder()
                                        .add(ConstantValue.equipment_host, host)
                                        .add(ConstantValue.equipment_base, product.getEquipmentbase())
                                        .add(ConstantValue.employeeId,BaseActivity.bundle.getString(ConstantValue.employeeId))
                                        .add(ConstantValue.is_success,ConstantValue.phpIs_successYes)
                                        .build();

                                request = new Request.Builder()
                                        .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpAddGoodsUrl*/
                                                ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.javaLackOfProduct)//请求的url
                                        .post(formBody)
                                        .build();


                                //创建/Call
                                call = okHttpClient.newCall(request);
                                //加入队列 异步操作
                                call.enqueue(new Callback() {
                                    //请求错误回调方法
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                        Log.i(TAG, "onFailure: 连接失败!");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {


                                        if (ConstantValue.Okhttp_Back_Success== response.code() ) {
                                            ResponseBody responseBody = response.body();
                                            String s = responseBody.string();
                                            // System.out.println(response.body().string());
                                            JSONObject myJsonObject = null;
                                            JSONObject myJsonObject0 = null;
                                            JSONArray jsonArray;
                                            try {
                                                myJsonObject = new JSONObject(s);

                                                int code = myJsonObject.getInt("code");
                                                String msg = myJsonObject.getString("msg");


                                                if (ConstantValue.PHP_Back_Success == code) {


                                                    productDao.dbUpdateProductAddTime( equipment.getEquipmentbase());
                                                    Log.i(TAG, "onResponse: 添加货物成功");
                                                    showToastView("添加货物成功!!");

                                                } else if (ConstantValue.PHP_Back_Fail == code) {
                                                    showToastView(msg);
                                                }

                                            } catch (Exception e) {
                                            }
                                            Log.i(TAG, "onResponse: "+s);
                                        }else {
                                        }
                                    }
                                });

                                openActivity(operationManagementActivity.class);
                                AppManager.getInstance().killActivity(operationManagementActivity.class);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                openActivity(operationManagementActivity.class);
                                AppManager.getInstance().killActivity(operationManagementActivity.class);
                                dialog.dismiss();
                            }
                        });


                        dialog = builder.create();
                        dialog.show();


                    }
                });



                buttonToNew.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //这样可以获取按钮的id
                        //修改密码，更新数据库之后，刷新一下这个TableActivity
                     if(null==(product.getPrematchProductname())||"".equals(product.getPrematchProductname()))
                     {
                         showToastView("没有更新数据！");

                     }else {
                         product.getPrematchProductname().equals(product.getProductName());
                         AlertDialog MyDialog = new AlertDialog.Builder(operationManagementActivity.this).create();//创建对话框


                         AlertDialog.Builder builder = new AlertDialog.Builder(operationManagementActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                         builder.setTitle("更新产品信息");
                         builder.setMessage("确定更新设备产品吗?修改后不可恢复！");

                         builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                                 // int product_total= 1000+product.getProductDaysum();
                                 Log.i("设备", product.getEquipmentbase());

                           /*     if(product.getProductName().equals(product.getPrematchProductname())&&product.getImgUrl().equals(product.getPrematchImgurl())){

                                }else{*/

                                 okHttpClient = new OkHttpClient.Builder()
                                         .connectTimeout(10, TimeUnit.SECONDS)
                                         .writeTimeout(10, TimeUnit.SECONDS)
                                         .readTimeout(20, TimeUnit.SECONDS)
                                         .build();

                                 //post方式提交的数据
                                 formBody = new FormBody.Builder()
                                         .add(ConstantValue.equipment_host, host)
                                         .add(ConstantValue.equipment_base, product.getEquipmentbase())
                                         .add(ConstantValue.employeeId,BaseActivity.bundle.getString(ConstantValue.employeeId))
                                         // .add(ConstantValue.platform,ConstantValue.phpAndroid)//PHP
                                         //.add(ConstantValue.phpAndroid,ConstantValue.phpVersion) // PHP
                                         .build();

                                 request = new Request.Builder()
                                         .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.javaReadyProductList)//请求的url
                                         .post(formBody)
                                         .build();


                                 //创建/Call
                                 call = okHttpClient.newCall(request);
                                 //加入队列 异步操作
                                 call.enqueue(new Callback() {
                                     //请求错误回调方法
                                     @Override
                                     public void onFailure(Call call, IOException e) {

                                         Log.i(TAG, "onFailure: 连接失败!");
                                     }

                                     @Override
                                     public void onResponse(Call call, Response response) throws IOException {


                                         if (ConstantValue.Okhttp_Back_Success== response.code() ) {
                                             ResponseBody responseBody = response.body();
                                             String s = responseBody.string();
                                             // System.out.println(response.body().string());
                                             JSONObject myJsonObject = null;
                                             JSONObject myJsonObject0 = null;
                                             JSONArray jsonArray;
                                             try {
                                                 myJsonObject = new JSONObject(s);

                                                 int code = myJsonObject.getInt("code");
                                                 String msg = myJsonObject.getString("msg");


                                                 if (ConstantValue.PHP_Back_Success == code) {

                                                     productDao.dbUpdateProductName(product.getPrematchProductname(), equipment.getEquipmentbase(), product.getPrematchImgurl());
                                                     productDao.dbUpdateProductPrematch("","",equipment.getEquipmentbase(),equipment.getEquipmenthost());
                                                     showToastView("正在更新[" + equipment.getId() + "]的数据，请稍候！");



                                                 } else if (ConstantValue.PHP_Back_Fail == code) {

                                                     Toast.makeText(operationManagementActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                 }

                                             } catch (Exception e) {
                                             }
                                             Log.i(TAG, "onResponse: "+s);
                                         }

                                     }
                                 });
                              //   NetMessFromPhp.getAllMessFromPhp(context);//更新数据

                                 NetMessFromPhp.getJavaAll(context);//java

                                 productList= productDao.dbQueryAll();

                                 openActivity(MainActivity_main_systemCorrelation.class);
                                 AppManager.getInstance().killActivity(operationManagementActivity.class);
                                 dialog.dismiss();
                             }
                         }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 productList= productDao.dbQueryAll();
                                 openActivity(MainActivity_main_systemCorrelation.class);
                                 AppManager.getInstance().killActivity(operationManagementActivity.class);
                                 dialog.dismiss();
                             }
                         });


                         dialog = builder.create();
                         dialog.show();
                     }

                    }

                });



                tableRow.addView(buttonOpenDoor);//将这个按钮添加到这行中
                tableRow.addView(buttonCloseDoor);//将这个按钮添加到这行中
                tableRow.addView(buttonToNew);//将这个按钮添加到这行中
                tableRow.addView(buttonAddProduct);//将这个按钮添加到这行中


                // 新建的TableRow添加到TableLayout
                tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }

        operationManagement.addView(tableLayout1, 1);//把这个表格放到activity_table.xml中tag=2的View那个位置


    }

    //返回上一个页面
    public void update_system_baseinfo(View view) {

        AppManager.getInstance().killActivity(operationManagementActivity.class);
        openActivity(MainActivity_main_systemCorrelation.class);
    }




    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override

    protected void onResume() {

        super.onResume();
        timeStart();


    }



    //region 无操作 返回主页

    private void timeStart() {

        new Handler(getMainLooper()).post(new Runnable() {

            @Override
            public void run() {

                backMain.start();

            }
        });
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                backMain.start();
                break;
            //否则其他动作计时取消
            default:
                backMain.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        backMain.cancel();
    }
}
