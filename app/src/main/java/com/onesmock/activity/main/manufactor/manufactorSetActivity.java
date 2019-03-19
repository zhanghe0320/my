package com.onesmock.activity.main.manufactor;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import com.onesmock.R;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.author.AuthorActivity;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.WifiActivity2;
import com.onesmock.activity.main.administratorInformation.wifi.wifiActivity;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.dao.SystemValues.SystemValues;

import java.util.ArrayList;

public class manufactorSetActivity extends BaseActivityO {
    private static final String TAG = "manufactorSetActivity";
    //线性布局
    private LinearLayout manufactor_LinearLayout;
    //返回按钮
    public enum KeyID{btn_pedOK,btn_pwdNO};

    ArrayList<SystemValues> systemValuesList = null;

    public void init(){
        manufactor_LinearLayout= (LinearLayout)findViewById(R.id.manufactor_LinearLayout);

    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufactorset);
        SystemValues valueSystem=  systemValuesDao.dbQueryOneByName(ConstantValue.localNumber);
        init();



        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, manufactorSetActivity.this);

         systemValuesList=new ArrayList<>(systemValuesDao.dbQueryAll()) ;
        //添加的textView1放到activity_table.xml中tag=1的View那个位置
        //如果用户数量为0就不用搞这么东西添加表格布局了
        if (systemValuesList.size() > 0) {

            TableLayout tableLayout1 = new TableLayout(this);//新建一个表格布局
            tableLayout1.setStretchAllColumns(true);//自动宽度，使表格在横向占据100%
            //打印表头
            TableRow tableRow = new TableRow(this);//新建一行

            TextView textView = new TextView(this);//新建一个TextView
            textView.setTextSize(30);//设置字体
            textView.setText("信息修改");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);//放到行中，自动增加一个单元格
            textView.setBackgroundColor(Color.parseColor("#2f7d67"));
            textView = new TextView(this);

            textView.setTextSize(30);
            textView.setText("相关参数");
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.parseColor("#2f7d67"));
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(30);
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
            for (int i = 0; i < systemValuesList.size(); i++) {
                final SystemValues systemValues1 = systemValuesList.get(i);

                // 一个用户占据一行
                tableRow = new TableRow(this);

                textView = new TextView(this);
                textView.setTextSize(25);//设备名称
                textView.setText(systemValues1.getName());
                tableRow.addView(textView);
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);

                textView = new TextView(this);
                textView.setTextSize(25);//设备编号
                textView.setText(systemValues1.getValue());
                tableRow.addView(textView);
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);

                Button button = new Button(this);
                button.setText("修改");
                button.setTextSize(25);//操作
                button.setTextColor(Color.parseColor("#FF0000"));
                textView.setBackgroundColor(Color.parseColor("#2f7d67"));
                textView.setGravity(Gravity.CENTER);
                textView.setId(systemValues1.getId());
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //这样可以获取按钮的id

                        //修改密码，更新数据库之后，刷新一下这个TableActivity

                         if((ConstantValue.wifi).equals(systemValues1.getName())){
                            Bundle bundle = new Bundle();
                            bundle.putString("name", systemValues1.getName());
                            bundle.putString("value", systemValues1.getValue());
                             openActivity(WifiActivity2.class,bundle);
                             AppManager.getInstance().killActivity(manufactorSetActivity.class);

                         }else{

                            Bundle bundle = new Bundle();
                            bundle.putString("name", systemValues1.getName());
                            bundle.putString("value", systemValues1.getValue());
                            openActivity(manufactorSetActivityUpdate.class,bundle);
                            AppManager.getInstance().killActivity(manufactorSetActivity.class);

                         }
                    }
                });

                tableRow.addView(button);//将这个按钮添加到这行中
                // 新建的TableRow添加到TableLayout
                tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
            manufactor_LinearLayout.addView(tableLayout1, 2);//把这个表格放到activity_table.xml中tag=2的View那个位置


        }


    }



    //返回上一个页面
    public void update_system_baseinfo(View view) {

        AppManager.getInstance().killActivity(manufactorSetActivity.class);
        openActivity(MainActivity_main.class);

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
