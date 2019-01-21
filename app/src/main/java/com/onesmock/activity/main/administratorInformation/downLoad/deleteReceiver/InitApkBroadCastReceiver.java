package com.onesmock.activity.main.administratorInformation.downLoad.deleteReceiver;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
public class InitApkBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "InitApkBroadCastReceive";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            comm.rmoveFile("http://192.168.1.106:8080/oneSmock/data/app.apk");
          //  Toast.makeText(context , "监听到系统广播添加" , Toast.LENGTH_LONG).show();
            Log.i(TAG, "onReceive: 监听到系统广播添加");
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            comm.rmoveFile("http://192.168.1.106:8080/oneSmock/data/app.apk");
            //Toast.makeText(context , "监听到系统广播移除" , Toast.LENGTH_LONG).show();
            Log.i(TAG, "onReceive: 监听到系统广播移除");
          //  System.out.println("");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            comm.rmoveFile("http://192.168.1.106:8080/oneSmock/data/app.apk");
            Toast.makeText(context , "监听到系统广播替换" , Toast.LENGTH_LONG).show();
            Log.i(TAG, "onReceive: 监听到系统广播替换");
        }
    }

}
