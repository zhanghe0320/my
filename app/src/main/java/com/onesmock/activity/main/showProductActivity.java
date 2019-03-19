package com.onesmock.activity.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.onesmock.R;
import com.onesmock.Util.banner.Banner;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivityO;


import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;

/**
 *产品详情页面只有15秒时间，无论是否触控全部返回。
 */
public class showProductActivity extends BaseActivityO {

    private static final String TAG = "showProductActivity";
    private AlertDialog dialog;
    //按钮设定

    public static Handler mainHandler;
    private Banner banner;
    private List<String> list;

    private LinearLayout update_system_baseinfo_xml;
    private Spinner mDevList;
    private SerialPortFinder mSerialPortFinder = new SerialPortFinder();


    private TextView productmesstext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showproduct);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 15, 1000, showProductActivity.this);//20秒后返回


        //   mDevList = (Spinner) findViewById(R.id.spinnercomno);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //从intent对象中把封装好的数据取出来
        String name= null;
        if(null==bundle||("").equals(bundle)){
            //name = ConstantValue.netAddress;
        }else{
            name = bundle.getString("equipmentbase");
        }

        product = productDao.dbQueryOneProduct(name);

        productmesstext = (TextView)findViewById(R.id.productmesstext);
        list =  new ArrayList<>();
        list.add(product.getImgUrl());
        String textMess = "";
        textMess = textMess + "       ";


        productmesstext.setText(textMess+product.getProductMess());//产品描述信息


        initView();
        Log.i(TAG, "onCreate: "+name+"-----------------------------");



    }


    //广告信息处理  首页轮播广告  从数据库读取的图片链接地址
    private void initView() {
        banner = (Banner) findViewById(R.id.banner);


        banner.setDataList(list);
        banner.setImgDelyed(5000);
        banner.startBanner();
        banner.startAutoPlay();


        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    // print(msg.obj.toString());
                    Log.i(TAG, "handleMessage: "+msg.obj.toString());
                }
            }

        };
    }


    @Override
    protected void onResume() {

        timeStart();
        super.onResume();


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
     * @return
     */
  /*  @Override
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
        backMain.start();
        return super.dispatchTouchEvent(ev);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        //backMain.cancel();
    }
}
