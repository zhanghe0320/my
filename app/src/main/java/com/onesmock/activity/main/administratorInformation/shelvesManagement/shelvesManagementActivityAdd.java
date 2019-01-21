package com.onesmock.activity.main.administratorInformation.shelvesManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.dialog.ConfirmDialog;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.netAddress.internetInformationActivity;
import com.onesmock.activity.main.administratorInformation.operationManagement.operationManagementActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.R;

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
public class shelvesManagementActivityAdd extends BaseActivityO {


    private static final String TAG = "shelvesManagementActivi";
    private EditText edit_equipment_num;

    public void init() {
        edit_equipment_num = (EditText) findViewById(R.id.edit_equipment_num);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelvesmanagementadd);
        init();
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页


    }


    //保存新的信息
    public void shelvesManagement_add(View view) {

        Equipment equipmentA = equipmentDao.dbQueryOneByEquipmentbase(edit_equipment_num.getText().toString());//通过编号找设备
            if (null == edit_equipment_num.getText().toString() || "".equals(edit_equipment_num.getText().toString())) {
            Log.i(TAG, "system_baseinfo_update_save: 设备号码或者设备名称不能为空！");
            //   || null == edit_equipment_name.getText().toString() ||"".equals(edit_equipment_name.getText().toString())
            showToastView("设备号码或者设备名称不能为空！");
        } else if (null == equipmentA ) {
            //  && null == equipmentA0    &&null == equipmentB    && null == equipmentB0




                ConfirmDialog confirmDialog = new ConfirmDialog(context, "确定添加设备吗？", "确认", "取消");
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        // TODO Auto-generated method stub

                        okHttpClient  = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10,TimeUnit.SECONDS)
                                .readTimeout(20, TimeUnit.SECONDS)
                                .build();

                        //post方式提交的数据
                        formBody = new FormBody.Builder()
                                .add(ConstantValue.equipment_host, systemValuesDao.dbQueryOneByPass(ConstantValue.localNumber).getValue())
                                .add(ConstantValue.equipment_base, edit_equipment_num.getText().toString())
                                .add(ConstantValue.employeeId,BaseActivity.bundle.getString(ConstantValue.employeeId))
                                .build();

                        request = new Request.Builder()
                                .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaBindShelf)//请求的url
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
                                    if("1".equals(s)){//成功
                                        String name = "设备"+equipmentDao.dbQueryAll().size();
                                        equipmentDao.dbInsertEquipment(name, edit_equipment_num.getText().toString(), systemValuesDao.dbQueryOneByPass(ConstantValue.localNumber).getValue());
                                        productDao.dbInsertProduct(systemValuesDao.dbQueryOneByPass(ConstantValue.localNumber).getValue(),edit_equipment_num.getText().toString());
                                        Log.i(TAG, "onResponse: 添加成功");
                                        AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                                        openActivity(shelvesManagementActivity.class);
                                    }else if("0".equals(s)){//失败
                                        Log.i(TAG, "onResponse: 添加失败");
                                        AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                                        openActivity(shelvesManagementActivity.class);
                                    }else {//货架输错，不存在
                                        Log.i(TAG, "onResponse: 货架不存在");
                                        AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                                        openActivity(shelvesManagementActivity.class);
                                    }
                                 /*   JSONObject myJsonObject= null;
                                    JSONObject myJsonObject0= null;
                                    JSONArray jsonArray;
                                    try{
                                        myJsonObject= new JSONObject(s);

                                        int code = myJsonObject.getInt("code");
                                        String msg = myJsonObject.getString("msg");
                                        if(ConstantValue.PHP_Back_Success==code){


                                            String name = "设备"+equipmentDao.dbQueryAll().size();
                                            equipmentDao.dbInsertEquipment(name, edit_equipment_num.getText().toString(), systemValuesDao.dbQueryOneByPass(ConstantValue.localNumber).getValue());
                                            productDao.dbInsertProduct(systemValuesDao.dbQueryOneByPass(ConstantValue.localNumber).getValue(),edit_equipment_num.getText().toString());

                                            showToastView(msg);
                                            AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                                            openActivity(shelvesManagementActivity.class);
                                        }else if(ConstantValue.PHP_Back_Fail==code){
                                            // Toast.makeText(context, "信息处理 添加设备失败！！"+msg, Toast.LENGTH_SHORT).show();
                                            Log.i(TAG, "onResponse:信息处理 添加设备失败！！ ");
                                            AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                                            openActivity(shelvesManagementActivity.class);
                                            showToastView(msg);

                                        }


                                    }catch (Exception e){

                                    }*/

                                }

                            }
                        });
                        confirmDialog.dismiss();

                        AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
                        openActivity(shelvesManagementActivity.class);
                        //toUserHome(context);
                        // AppManager.getAppManager().AppExit(context);
                    }

                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });

        } else {
        }
    }


    //设备  返回上一个页面
    public void update_system_baseinfo(View view) {
        AppManager.getInstance().killActivity(shelvesManagementActivityAdd.class);
        openActivity(shelvesManagementActivity.class);

    }

    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override

    protected void onResume() {
        timeStart();
        super.onResume();



    }


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