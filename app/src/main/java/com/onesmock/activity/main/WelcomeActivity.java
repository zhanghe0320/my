package com.onesmock.activity.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.onesmock.R;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.main.administratorInformation.wifi.WLANListener;
import com.onesmock.activity.main.administratorInformation.wifi.WifiUtils;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.iv_entry)
    ImageView mIVEntry;




    private static final int ANIM_TIME = 2000;

    private static final float SCALE_END = 1.15F;
    private Context context;

    private static final String TAG = "WelcomeActivity";
    private static final int[] Imgs={
            R.drawable.lsjwelcome
 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
    /*    if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }*/
       context = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG, "run: 延迟5秒扫描wifi!");
                        Message message = Message.obtain();
                        message.obj="listener";
                        handler.sendMessage(message);
                    }

                }).start();
            }
        }, 5*1000);



        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startMainActivity();

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        WLANListener.unregister();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            if(null == listener){
                listener = new WLANListener(context);

            }

            listener.register(new WLANListener.WLANStateListener() {
                @Override
                public void onStateChanged() {

                }

                @Override
                public void onStateDisabled() {

                }

                @Override
                public void onStateDisabling() {

                }

                @Override
                public void onStateEnabled() {
                    if(!mUtils.isConnectWifi()){
                        new WelcomeActivity.MyAsyncTask().execute();
                    }else{
                    }
                }

                @Override
                public void onStateEnabling() {

                }

                @Override
                public void onStateUnknow() {

                }
            });


            mUtils = new WifiUtils(context);
            if(!mUtils.isWifiOPened()){
                mUtils.EnableWifi();
            }

        }
    };
    private void startMainActivity(){
        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        mIVEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);


        Observable.timer(15*1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {

                    @Override
                    public void call(Long aLong)
                    {
                        startAnim();
                    }
                });
    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //扫描附近WIFI信息
            //showDialog();
            //scanResults = mUtils.getScanWifiResult();

            return null;
        }

        @Override
        protected void onPostExecute(Void resul) {
            super.onPostExecute(resul);
            //progressDismiss();
            initListViewData();
        }
    }
    private void initListViewData() {
        if(!mUtils.isConnectWifi()){
            SystemValuesDao systemValuesDao = new SystemValuesDao(context);
            SystemValues systemValues = systemValuesDao.dbQueryOneByID(ConstantValue.codeValue0);
            mUtils.connectWifiTest(systemValues.getName(),systemValues.getValue());
            Log.i(TAG, "initListViewData: "+systemValues.getValue()+"--------"+systemValues.getName());
        }
       /* Message msg = Message.obtain();
        msg.what = MSG_LOAD_GRID;
        mHandler.sendMessage(msg);*/

    }

}
