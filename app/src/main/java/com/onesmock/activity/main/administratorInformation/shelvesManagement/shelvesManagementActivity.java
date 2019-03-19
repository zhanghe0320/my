package com.onesmock.activity.main.administratorInformation.shelvesManagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.operationManagement.operationManagementActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.product.Product;
import com.onesmock.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class shelvesManagementActivity extends BaseActivityO {
    private static final String TAG = "shelvesManagementActivi";
    //线性布局
    private LinearLayout shelvesManagement_LinearLayout;
    public enum KeyID{btn_pedOK,btn_pwdNO};
    public void init(){

        shelvesManagement_LinearLayout= (LinearLayout)findViewById(R.id.shelvesManagement_LinearLayout);


    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelvesmanagement);

      //  NetMessFromPhp.getAllMessFromPhp(context);//更新数据
        NetMessFromPhp.getJavaAll(context);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页

        init();


        ArrayList<Equipment> equipmentList = equipmentDao.dbQueryAll();//查出表中的所有用户放到一个ArrayList中

        //添加的textView1放到activity_table.xml中tag=1的View那个位置
        //如果用户数量为0就不用搞这么东西添加表格布局了
        if (equipmentList.size() > 0) {

            TableLayout tableLayout1 = new TableLayout(this);//新建一个表格布局
            tableLayout1.setStretchAllColumns(true);//自动宽度，使表格在横向占据100%
            //打印表头
            TableRow tableRow = new TableRow(this);//新建一行

            TextView textView = new TextView(this);//新建一个TextView
            textView.setTextSize(25);//设置字体
            textView.setText("设备序号");
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.parseColor("#2f7d67"));
            tableRow.addView(textView);//放到行中，自动增加一个单元格

            textView = new TextView(this);

            textView.setTextSize(25);
            textView.setText("设备编号");
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.parseColor("#2f7d67"));
            tableRow.addView(textView);




            textView = new TextView(this);
            textView.setTextSize(24);
            textView.setText("操作");
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.parseColor("#2f7d67"));
            tableRow.addView(textView);

            tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            /////单独放置设置信息本及设备号码

            EditText editText;

            //打印用户信息
            for (int i = 0; i < equipmentList.size(); i++) {
                final Equipment equipment = equipmentList.get(i);

                // 一个用户占据一行
                tableRow = new TableRow(this);

                textView = new TextView(this);
                textView.setTextSize(25);//设备名称
                textView.setText(equipment.getEquipmentName());
                tableRow.addView(textView);
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);

                textView = new TextView(this);
                textView.setTextSize(25);//设备编号
                textView.setText(equipment.getEquipmentbase());
                tableRow.addView(textView);
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);

              //  final Product product =productList.get(i);


                Button buttonDelete = new Button(this);
                buttonDelete.setText("删除");
                buttonDelete.setTextSize(25);//操作
                if(ConstantValue.mainEquipment.equals(equipment.getEquipmentName())){
                    buttonDelete.setEnabled(false);

                }
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);


                buttonDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //这样可以获取按钮的id
                        //修改密码，更新数据库之后，刷新一下这个TableActivity

                        equipmentDao.dbDeleteEquipment(equipment.getEquipmentbase(),equipment.getEquipmenthost());
                        productDao.dbDeleteProduct(equipment.getEquipmenthost(),equipment.getEquipmentbase());

                        showToastView("正在删除["+equipment.getId()+"]，请稍候！");
                      //  NetMessFromPhp.getAllMessFromPhp(context);
                        NetMessFromPhp.getJavaAll(context);//java

                        openActivity(shelvesManagementActivity.class);
                        AppManager.getInstance().killActivity(shelvesManagementActivity.class);


                    }
                });
                // 新建的TableRow添加到TableLayout

                tableRow.addView(buttonDelete);//将这个按钮添加到这行中
                tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
            shelvesManagement_LinearLayout.addView(tableLayout1, 1);//把这个表格放到activity_table.xml中tag=2的View那个位置

        }

    }


    //添加设备
    public void shelvesManagement_add(View view) {
        openActivity(shelvesManagementActivityAdd.class);
        AppManager.getInstance().killActivity(shelvesManagementActivity.class);

    }
    //返回上一个页面
    public void update_system_baseinfo(View view) {
        AppManager.getInstance().killActivity(shelvesManagementActivity.class);
        openActivity(MainActivity_main_systemCorrelation.class);

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
