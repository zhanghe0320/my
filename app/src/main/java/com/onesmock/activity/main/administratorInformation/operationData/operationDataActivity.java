package com.onesmock.activity.main.administratorInformation.operationData;

import android.app.AlertDialog;
import android.content.Intent;
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


import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.author.AuthorActivity;
import com.onesmock.activity.main.administratorInformation.netAddress.internetInformationActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.dao.product.Product;
import com.onesmock.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class operationDataActivity extends BaseActivityO {

    private static final String TAG = "operationDataActivity";
    //线性布局
    private LinearLayout product_LinearLayout;

    public void init() {
        //注册组件
        product_LinearLayout = (LinearLayout) findViewById(R.id.product_LinearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        //去除title
        setContentView(R.layout.activity_operationdata);//加载进来

        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, operationDataActivity.this);
        init();

        //求出用户的数量，并且显示
        int producrSize = productDao.dbGetProductSize();
       /* TextView textView1 = new TextView(this);
        textView1.setText("共有" + (producrSize ) + "种商品");
        textView1.setTextSize(25);
        product_LinearLayout.addView(textView1, 1);*///添加的textView1放到activity_table.xml中tag=1的View那个位置
        //如果用户数量为0就不用搞这么东西添加表格布局了
        if (producrSize > 0) {
            ArrayList<Product> productList = productDao.dbQueryAll();//查出表中的所有用户放到一个ArrayList中
            TableLayout tableLayout1 = new TableLayout(this);//新建一个表格布局
            tableLayout1.setStretchAllColumns(true);//自动宽度，使表格在横向占据100%
            //打印表头
            TableRow tableRow = new TableRow(this);//新建一行

            TextView textView = new TextView(this);//新建一个TextView
            textView.setTextSize(25);//设置字体
            textView.setText("设备号");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);//放到行中，自动增加一个单元格
            textView.setGravity(Gravity.CENTER);


            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("商品");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("日出货量");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("总出货量");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("最后更新时间");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);


            tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            /////单独放置设置信息本及设备号码
            EditText editText;

            //打印用户信息
            for (int i = 0; i < producrSize; i++) {

                Product product = productList.get(i);
                // 一个用户占据一行
                tableRow = new TableRow(this);

                textView = new TextView(this);
                textView.setTextSize(25);//设备号
                textView.setText(String.valueOf(i+1));
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);

                textView = new TextView(this);
                textView.setTextSize(25);//产品名字
                textView.setText(product.getProductName());
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);

                textView = new TextView(this);
                textView.setTextSize(25);//日出货
                textView.setText(product.getProductDaysum());
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);


                textView = new TextView(this);
                textView.setTextSize(25);//总出货量
                textView.setText(product.getProductTotal());
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);


                long timeStamp = System.currentTimeMillis();  //获取当前时间戳,也可以是你自已给的一个随机的或是别人给你的时间戳(一定是long型的数据)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");//这个是你要转成后的时间的格式
                String sd = sdf.format(new Date(Long.parseLong(String.valueOf(product.getCreatedTime()))));   // 时间戳转换成时间


                textView = new TextView(this);
                textView.setTextSize(25);//上架时间
                if (null == product.getCreatedTime()) {
                    sd = sdf.format(new Date(timeStamp));
                    textView.setText(sd);
                } else {

                    textView.setText(sd);
                }
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);

 /*
                tableRow.addView(button);*///将这个按钮添加到这行中
                // 新建的TableRow添加到TableLayout
                tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
            product_LinearLayout.addView(tableLayout1, 1);//把这个表格放到activity_table.xml中tag=2的View那个位置
        }
        //返回MainActivity

    }


    //返回上一个页面
    public void update_system_baseinfo(View view) {
        AppManager.getInstance().killActivity(operationDataActivity.class);
        openActivity(MainActivity_main_systemCorrelation.class);

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
