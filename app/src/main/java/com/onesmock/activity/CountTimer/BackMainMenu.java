/*
package com.onesmock.activity.CountTimer;

import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.logging.Handler;

public class BackMainMenu {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case hide1: {
                    button.setVisibility(View.GONE);
                    break;
                }
                case hide2:
                    break;
            }
        }
    };

    private void restTime() {
        if (mHandler.hasMessages(hide1) || mHandler.hasMessages(hide2)) {
            mHandler.removeMessages(hide1);
            mHandler.removeMessages(hide2);
        }
        Message msg = mHandler.obtainMessage(hide1);
        mHandler.sendMessageDelayed(msg, 3 * 1000);
    }


    //使用键盘或者遥控器用dispatchKeyEvent
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                mHandler.removeMessages(hide1);
            }
            case KeyEvent.ACTION_UP: {
                restTime();
                break;
            }


        }
        return super.onTouchEvent(event);
    }


    //使用触摸用onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mHandler.removeMessages(hide1);
            }
            case MotionEvent.ACTION_UP: {
                restTime();
                break;
            }


        }
        return super.onTouchEvent(event);
    }

}
*/
