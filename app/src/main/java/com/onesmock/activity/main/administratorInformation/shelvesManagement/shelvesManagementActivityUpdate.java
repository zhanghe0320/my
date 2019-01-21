package com.onesmock.activity.main.administratorInformation.shelvesManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.product.Product;
import com.onesmock.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class shelvesManagementActivityUpdate extends BaseActivityO {
    private AlertDialog dialog;
    TextView factory_update_equipmentName;//设置设备名称
    EditText factory_update_equipmentnum;//设置显示数据
    EditText factory_product_equipmentName;//设置显示数据
    Button factory_update_equipmentsave;//获取按钮
    Button factory_update_equipment_equipmentNum;
    private static final String TAG = "shelvesManagementActivi";

    public void init() {

        factory_update_equipmentName = (TextView) findViewById(R.id.factory_update_equipmentName);//设置设备名称
        factory_update_equipmentnum = (EditText) findViewById(R.id.factory_update_equipmentnum);//设置显示数据
        factory_product_equipmentName = (EditText) findViewById(R.id.factory_product_equipmentName);//设置显示数据
        factory_update_equipmentsave = (Button) findViewById(R.id.factory_update_equipmentsave);//获取按钮
        factory_update_equipment_equipmentNum = (Button) findViewById(R.id.factory_update_equipment_equipmentNum);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        setContentView(R.layout.activity_shelvesmanagementupdate);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页

        Intent intent = getIntent();
        init();
        Bundle bundle = intent.getExtras();
        //从intent对象中把封装好的数据取出来
        final String equipmenthost = bundle.getString("equipmenthost");
        final String equipmentbase = bundle.getString("equipmentbase");
        final String equipmentName = bundle.getString("equipmentName");
        final String equipment_id  = bundle.getString("equipment_id");
        final String product_id  = bundle.getString("product_id");

        final int equipmentid = Integer.valueOf(equipment_id).intValue();
        final int productid = Integer.valueOf(product_id).intValue();

        Product product = productDao.dbQueryOneProduct(equipmentbase);
        Equipment equipment1 = equipmentDao.dbQueryOneByEquipmentbase(equipmentbase);


        factory_update_equipmentName.setGravity(Gravity.CENTER);
        factory_update_equipmentName.setText(equipment1.getEquipmentName());


        factory_update_equipmentName.setGravity(Gravity.CENTER);
        factory_update_equipmentnum.setText(equipment1.getEquipmentbase());
   /*     if ("主设备".equals(equipment1.getEquipmentName())){
            update_equipmentnum.setEnabled(false);
     }*/


        factory_update_equipmentName.setGravity(Gravity.CENTER);
        factory_product_equipmentName.setEnabled(false);
        if(null==product.getProductName()||"".equals(product.getProductName())){
            factory_product_equipmentName.setText("");
        }else {
            factory_product_equipmentName.setText(product.getProductName());
        }

        factory_update_equipmentsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

          AlertDialog.Builder builder = new AlertDialog.Builder(shelvesManagementActivityUpdate.this);
                builder.setTitle("修改设备信息");
                builder.setMessage("确定删除吗?修改后不可恢复！");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText editText = (EditText) findViewById(R.id.factory_update_equipmentnum);//找到数据
                        EditText editText1 = (EditText) findViewById(R.id.factory_product_equipmentName);//找到数据

                        int a =  equipmentDao.dbQueryOne(equipmenthost,editText.toString());
                        int b = productDao.dbQueryOne(equipmenthost,editText.toString());
                        if(0==a || 0 == b){
                            Long time = System.currentTimeMillis();

                            equipmentDao.dbUpdateEquipment(editText.getText().toString(), equipmenthost, time,equipmentid);
                            productDao.dbUpdateProduct(editText.getText().toString(), equipmenthost,productid);
                            finish();
                            Intent intent = new Intent(shelvesManagementActivityUpdate.this, shelvesManagementActivity.class);
                            startActivity(intent);

                            dialog.dismiss();
                        }else{
                            Intent intent = new Intent(shelvesManagementActivityUpdate.this, shelvesManagementActivityUpdate.class);
                            Toast.makeText(context, "此设备已经存在，请勿重复添加！请检查！。。", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        //返回MainActivity
        factory_update_equipment_equipmentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

           /*     Intent intent = new Intent(shelvesManagementActivityUpdate.this, shelvesManagementActivity.class);
                startActivity(intent);*/
                AppManager.getInstance().killActivity(shelvesManagementActivityUpdate.class);
                finish();
            }
        });

    }

    public void update_equipmentsave(View view) {

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