package com.onesmock.activity.main.manufactor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.FileUtils;
import com.onesmock.Util.dialog.ConfirmDialog;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.R;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;



import android_serialport_api.SerialPortFinder;


public class manufactorSetActivityUpdate  extends BaseActivityO {
    private static final String TAG = "manufactorSetActivityUp";
    TextView manufactor_systemvalues_name;//设置设备名称
    EditText manufactor_systemvalues_value;//设置显示数据
    private Spinner mDevList;//串口列表
    private SerialPortFinder mSerialPortFinder = new SerialPortFinder();//串口号码

    public void init(){

        manufactor_systemvalues_name = (TextView) findViewById(R.id.manufactor_systemvalues_name);//参数名字
        manufactor_systemvalues_value= (EditText) findViewById(R.id.manufactor_systemvalues_newvalue);//参数的值
        mDevList = (Spinner) findViewById(R.id.spinnercomno);//串口列表

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        setContentView(R.layout.activity_manufactorsetupdate);
        //activity 管理
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, manufactorSetActivityUpdate.this);
        init();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //从intent对象中把封装好的数据取出来
        final  String name = bundle.getString("name");
        final  String value = bundle.getString("value");


        SystemValues systemValues=systemValuesDao.dbQueryOneByName(name);
        Log.i("名字",systemValues.getName()+systemValues.getEquipmenthost()+systemValues.getValue());

        if (ConstantValue.serialPort.equals(name)) {//如果是串口号码设置 文本输入框不显示  不占控件
            Spinner spinner = new Spinner(this);
            manufactor_systemvalues_value.setVisibility(View.GONE);
        } else {
            mDevList.setVisibility(View.GONE);
        }

        if (ConstantValue.serialPort.equals(name)) {//串口号码进行选择
            //串口号码进行选择
            //  mDevList = (Spinner)findViewById(R.id.spinnercomno);
            String[] items = mSerialPortFinder.getAllDevicesPath();
            String[] items1 = new String[items.length + 1];
            for (int i = 0; i < items.length; i++) {
                items1[i + 1] = items[i];
            }
            items1[0] = "---请选择---";
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1); //第二个参数表示spinner没有展开前的UI类型

            mDevList.setAdapter(aa); //之前已经通过Spinner spin = (Spinner) findViewById(R.id.spinner);来获取spin对象
            int iposition = aa.getPosition(systemValuesDao.dbQueryOneByName(ConstantValue.serialPort).getValue());
            if (-1 == iposition) {
                mDevList.setSelection(0);
            } else {
                mDevList.setSelection(iposition);
            }
            // update_pass_value.setEnabled(false);
            manufactor_systemvalues_value.setText(systemValuesDao.dbQueryOneByName(ConstantValue.serialPort).getValue());
        }

        manufactor_systemvalues_name.setGravity(Gravity.CENTER);
        manufactor_systemvalues_name.setText(systemValues.getName());


        manufactor_systemvalues_value.setGravity(Gravity.CENTER);
        manufactor_systemvalues_value.setText(systemValues.getValue());





    }


    //设备 管理信息 进行货架的产品的更新
    public void system_baseinfo_update_save(View view) {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //从intent对象中把封装好的数据取出来
        final  String name = bundle.getString("name");
        final  String value = bundle.getString("value");
        final SystemValues systemValues1  = systemValuesDao.dbQueryOneByName(name);

        AlertDialog.Builder builder = new AlertDialog.Builder(manufactorSetActivityUpdate.this);
        builder.setTitle("修改设备信息");
        builder.setMessage("确定修改吗?修改后不可恢复！");

        ConfirmDialog confirmDialog = new ConfirmDialog(context, "确定修改吗?修改后不可恢复！?", "确认", "取消");
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                // TODO Auto-generated method stub

                EditText editText = (EditText) findViewById(R.id.manufactor_systemvalues_newvalue);//找到数据
                Long time=  System.currentTimeMillis();
                if("本机编号".equals(name)){

                    //更新主设备号码
                    productDao.dbUpdateOldToNew(ConstantValue.mainEquipment,editText.getText().toString(),editText.getText().toString());
                    equipmentDao.dbUpdateEquipmentToNew(ConstantValue.mainEquipment,editText.getText().toString(),editText.getText().toString(),systemValues1.getEquipmenthost());
                    //修改主机号码信息
                    systemValuesDao.dbUpdateEequipmenthost0(systemValues1.getEquipmenthost(),editText.getText().toString(),time,systemValues1.getId());
                    systemValuesDao.dbUpdateEequipmenthost(systemValues1.getEquipmenthost(),editText.getText().toString(),time,systemValues1.getId());
                    Log.i("本机编号","以前的编号"+systemValues1.getEquipmenthost()+"以后的编号"+editText.getText().toString());
                    //修改主机号码信息
                    productDao.dbDeleteProductEquipmenthost(editText.getText().toString());
                    equipmentDao.dbDeleteEquipmentEquipmenthost(editText.getText().toString());
                    authorDao.dbDeleteAuthorEquipmenthost(editText.getText().toString());
                 //   netMessFromPhp.getAllMessFromPhp(context);
                    NetMessFromPhp.getJavaAll(context);//java
                    NetMessFromPhp.getVideo(context);

                    FileUtils.DeleteFolder(xmppConnect.FILE_PATH);//删除二维码
                   // xmppConnect.disconnectAccount();
                    //  xmppConnect.disconnectAccount();
                    xmppConnect.xmppLogin(editText.getText().toString());



                    openActivity(manufactorSetActivity.class);
                    AppManager.getInstance().killActivity(manufactorSetActivityUpdate.class);



                }else if (ConstantValue.serialPort.equals(name)) {

                    systemValuesDao.dbUpdateSerialPort(mDevList.getSelectedItem().toString(),systemValues1.getEquipmenthost() ,time, systemValues1.getId());

                   // netMessFromPhp.getAllMessFromPhp(context);
                    NetMessFromPhp.getJavaAll(context);//java
                    NetMessFromPhp.getVideo(context);
                    FileUtils.DeleteFolder(xmppConnect.FILE_PATH);//删除二维码
                    // xmppConnect.disconnectAccount();
                    //  xmppConnect.disconnectAccount();
                    xmppConnect.xmppLogin(editText.getText().toString());

                    openActivity(manufactorSetActivity.class);
                    AppManager.getInstance().killActivity(manufactorSetActivityUpdate.class);
                }else{

                    systemValuesDao.dbUpdateValue(editText.getText().toString(), time, systemValues1.getId());

                    //netMessFromPhp.getAllMessFromPhp(context);
                    NetMessFromPhp.getJavaAll(context);//java
                    NetMessFromPhp.getVideo(context);
                    xmppConnect.getPHPIpAddress();
                    xmppConnect.getOpenfireIpAddress();
                    FileUtils.DeleteFolder(xmppConnect.FILE_PATH);//删除二维码
                    // xmppConnect.disconnectAccount();
                    //  xmppConnect.disconnectAccount();
                    xmppConnect.xmppLogin(editText.getText().toString());


                    openActivity(manufactorSetActivity.class);
                    AppManager.getInstance().killActivity(manufactorSetActivityUpdate.class);
                }




          /*      AppManager.getInstance().killActivity(MainActivity.class);
                openActivity(MainActivity.class);*/
                confirmDialog.dismiss();


        }

            @Override
            public void doCancel() {
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
            }
        });
    }


    //设备 管理信息 返回上一个页面
    public void update_system_baseinfo(View view) {
        AppManager.getInstance().killActivity(manufactorSetActivityUpdate.class);
        openActivity(manufactorSetActivity.class);

    }

    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        timeStart();
        super.onResume();

        onCreate(null);

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
