package com.onesmock.activity.CountTimer;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 监控页面是否有操作
 */
public class BaseDispatchTouchActivity extends AppCompatActivity {
    private CountTimer countTimerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }
    private void init() {
        //初始化CountTimer，设置倒计时为2分钟。
        countTimerView=new CountTimer(120000,1000,BaseDispatchTouchActivity.this);
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     * 使用触控
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:

                countTimerView.start();
                break;
            //否则其他动作计时取消
            default:
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    //使用键盘或者遥控器用dispatchKeyEvent
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


                switch (event.getAction()){

                 case KeyEvent.ACTION_DOWN:
                        countTimerView.cancel();
                   break;
                 case KeyEvent.ACTION_UP:
                       countTimerView.start();
                   break;
                }


             return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {

        super.onResume();
        timeStart();
    }
}