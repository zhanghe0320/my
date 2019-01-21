package com.onesmock.activity.BroadcastReceiver;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;


import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.activity.main.WelcomeActivity;

import java.util.List;

/*
*
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 *
 * 监听广播
*/


public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot ="android.intent.action.BOOT_COMPLETED";
    static final String action_boot0 ="android.media.AUDIO_BECOMING_NOISY";
    private static final String TAG = "BootBroadcastReceiver";
    private static boolean BOOT_COMPLETED = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 开机后一般会停留在锁屏页面且短时间内没有进行解锁操作屏幕会进入休眠状态，此时就需要先唤醒屏幕和解锁屏幕
        // 屏幕唤醒
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "StartupReceiver");//最后的参数是LogCat里用的Tag
        wl.acquire();
        // 屏幕解锁
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("StartupReceiver");//参数是LogCat里用的Tag
        kl.disableKeyguard();


        if (intent.getAction().equals(action_boot)||intent.getAction().equals(action_boot0)/*&&!BOOT_COMPLETED*//*&&intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")*/) {


            if(!BOOT_COMPLETED){
                Log.e(TAG, "onReceive: 开机启动");
                //开机启动
                Intent mainIntent = new Intent(context, WelcomeActivity.class);
                //在BroadcastReceiver中显示Activity，必须要设置FLAG_ACTIVITY_NEW_TASK标志
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                BOOT_COMPLETED = true;
            }

        }

    }

}
