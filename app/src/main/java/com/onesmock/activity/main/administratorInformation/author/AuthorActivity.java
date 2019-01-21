package com.onesmock.activity.main.administratorInformation.author;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.onesmock.R;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.dao.author.Author;

import java.util.ArrayList;


/**
 * 只能查看
 */
public class AuthorActivity extends BaseActivityO {


    private static final String TAG = "authorActivity";
    public enum KeyID{btn_pedOK,btn_pwdNO};
    private LinearLayout author_LinearLayout;
    BackMain backMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);//加载进来
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        author_LinearLayout = (LinearLayout) findViewById(R.id.author_LinearLayout);
        backMain = new BackMain(1000 * 30, 1000, AuthorActivity.this);//返回主页

        ArrayList<Author> authorList=  authorDao.dbQueryAll();

        if (authorList.size() > 0) {
            TableLayout tableLayout1 = new TableLayout(this);//新建一个表格布局
            tableLayout1.setStretchAllColumns(true);//自动宽度，使表格在横向占据100%
            //打印表头
            TableRow tableRow = new TableRow(this);//新建一行

            TextView textView = new TextView(this);//新建一个TextView
            textView.setTextSize(25);//设置字体
            textView.setText("姓名");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);//放到行中，自动增加一个单元格

            textView = new TextView(this);
            textView.setTextSize(25);
            textView.setText("电话");
            textView.setGravity(Gravity.CENTER);
            tableRow.addView(textView);

            tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            /////单独放置设置信息本及设备号码
            EditText editText;
            //打印用户信息

            for (int i = 0; i < authorList.size(); i++) {
                final Author author = authorList.get(i);
                Log.i(TAG, "onCreate: "+author.getAuthor_name()+"------------------------");

                // 一个用户占据一行
                tableRow = new TableRow(this);
                textView = new TextView(this);
                textView.setTextSize(25);//名字
                textView.setText(author.getAuthor_name());
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);

                textView = new TextView(this);
                textView.setTextSize(25);//设备信息
                textView.setText(author.getAuthor_phone());
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);

                tableLayout1.addView(tableRow, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            }
            author_LinearLayout.addView(tableLayout1, 2);//把这个表格放到activity_table.xml中tag=2的View那个位置
        }



    }


    //返回上一个页面
    public void update_system_baseinfo(View view) {
        openActivity(MainActivity_main_systemCorrelation.class);
        AppManager.getInstance().killActivity(AuthorActivity.class);

    }



    private void init() {
        // TODO: 五分钟无人操作回主页面 定时器 无人操作自动跳转
        //backMain = new BackMain(1000 * 60 * 5, 1000, this);


    }

    @Override
    protected void onResume() {
        timeStart();
        super.onResume();
        //onCreate(null);
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

