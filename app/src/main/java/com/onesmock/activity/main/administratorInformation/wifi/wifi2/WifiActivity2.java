package com.onesmock.activity.main.administratorInformation.wifi.wifi2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.onesmock.R;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.adapter.WifiListAdapter;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.app.AppContants;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.bean.WifiBean;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.dialog.WifiLinkDialog;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.onesmock.R;

public class WifiActivity2 extends BaseActivityO {

    private static final String TAG = "WifiActivity2";
    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean mHasPermission;

    ProgressBar pbWifiLoading;

    List<WifiBean> realWifiList = new ArrayList<>();

    private WifiListAdapter adapter;

    private RecyclerView recyWifiList;

    private WifiBroadcastReceiver wifiReceiver;

    private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）
    private Button wifi_back_systembase;

    private WifiManager mWiFiManager;
    /**
     * 开启WiFi
     *
     * @param view
     */
    public void openWiFi(View view) {
        // 判断是否已经打开WiFi
        if (!mWiFiManager.isWifiEnabled()) {
            // 打开Wifi连接
            mWiFiManager.setWifiEnabled(true);
            Log.i(TAG, "openWiFi: 开启");

            recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
            adapter = new WifiListAdapter(BaseActivity.context,realWifiList);
            recyWifiList.setLayoutManager(new LinearLayoutManager(this));
            recyWifiList.setAdapter(adapter);


         /*   if(WifiSupport.isOpenWifi(WifiActivity2.this) && mHasPermission){
                sortScaResult();//转换参数
            }else{
                // Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态或权限获取失败22222",Toast.LENGTH_SHORT).show();
                BaseActivity.showToastView("WIFI处于关闭状态或权限获取失败!");
            }*/

            adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View view, int postion, Object o) {
                    WifiBean wifiBean = realWifiList.get(postion);
                    if(wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)){
                        String capabilities = realWifiList.get(postion).getCapabilities();
                        if(WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                            WifiConfiguration tempConfig  = WifiSupport.isExsits(wifiBean.getWifiName(),WifiActivity2.this);
                            if(tempConfig == null){
                                WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                                WifiSupport.addNetWork(exsits, WifiActivity2.this);
                            }else{
                                WifiSupport.addNetWork(tempConfig, WifiActivity2.this);
                            }
                        }else{   //需要密码，弹出输入密码dialog
                            noConfigurationWifi(postion);
                        }
                    }
                }
            });
        }
    }

    /**
     * 关闭WiFi
     *
     * @param view
     */
    public void closeWiFi(View view) {
        if (mWiFiManager.isWifiEnabled()) {
            // 关闭Wifi连接
            mWiFiManager.setWifiEnabled(false);
            Log.i(TAG, "closeWiFi: 关闭");

            recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
            realWifiList.clear();
            adapter = new WifiListAdapter(BaseActivity.context,realWifiList);
            recyWifiList.setLayoutManager(new LinearLayoutManager(this));
            recyWifiList.setAdapter(adapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi2);
        //activity 管理
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页
        // 获取WiFi管理者对象
        mWiFiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        pbWifiLoading = (ProgressBar) this.findViewById(R.id.pb_wifi_loading);

        hidingProgressBar();
        mHasPermission = checkPermission();
        if (!mHasPermission && WifiSupport.isOpenWifi(WifiActivity2.this)) {  //未获取权限，申请权限
            requestPermission();
        }else if(mHasPermission && WifiSupport.isOpenWifi(WifiActivity2.this)){  //已经获取权限
            initRecycler();
        }else{
           // Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();
            BaseActivity.showToastView("WIFI处于关闭状态");
        }

        wifi_back_systembase= (Button)findViewById(R.id.wifi_back_systembase);

        wifi_back_systembase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getInstance().killActivity(WifiActivity2.class);
                openActivity(MainActivity_main_systemCorrelation.class);
            }
        });
    }

    private void initRecycler() {
        recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
        adapter = new WifiListAdapter(BaseActivity.context,realWifiList);
        recyWifiList.setLayoutManager(new LinearLayoutManager(this));
        recyWifiList.setAdapter(adapter);

        if(WifiSupport.isOpenWifi(WifiActivity2.this) && mHasPermission){
            sortScaResult();
        }else{
           // Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态或权限获取失败22222",Toast.LENGTH_SHORT).show();
            BaseActivity.showToastView("WIFI处于关闭状态或权限获取失败!");
        }

        adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Object o) {
                WifiBean wifiBean = realWifiList.get(postion);
                if(wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)){
                    String capabilities = realWifiList.get(postion).getCapabilities();
                    if(WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                        WifiConfiguration tempConfig  = WifiSupport.isExsits(wifiBean.getWifiName(),WifiActivity2.this);
                        if(tempConfig == null){
                            WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                            WifiSupport.addNetWork(exsits, WifiActivity2.this);
                        }else{
                            WifiSupport.addNetWork(tempConfig, WifiActivity2.this);
                        }
                    }else{   //需要密码，弹出输入密码dialog
                        noConfigurationWifi(postion);
                    }
                }
            }
        });
    }


    private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
        WifiLinkDialog linkDialog = new WifiLinkDialog(this,
                R.style.dialog_download,realWifiList.get(position).getWifiName(), realWifiList.get(position).getCapabilities());
        if(!linkDialog.isShowing()){
            linkDialog.show();
        }
    }

    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        timeStart();
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        this.registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        backMain.cancel();
        Log.i(TAG, "onDestroy: 注销WiFi监听");

        this.unregisterReceiver(wifiReceiver);
    }

    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED:{
                        Log.d(TAG,"已经关闭");
                        BaseActivity.showToastView("WIFI处于关闭状态!");

                       // Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        Log.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        Log.d(TAG,"已经打开");
                        sortScaResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        Log.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        Log.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    Log.d(TAG,"wifi没连接上");
                    hidingProgressBar();
                    for(int i = 0;i < realWifiList.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
                    }
                    adapter.notifyDataSetChanged();
                }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                    Log.d(TAG,"wifi连接上了");
                    hidingProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiActivity2.this);

                    //连接成功 跳转界面 传递ip地址
                    //Toast.makeText(WifiActivity2.this,"wifi连接上了",Toast.LENGTH_SHORT).show();
                    BaseActivity.showToastView("WIFI已经连接！");

                    connectType = 1;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType);
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    Log.d(TAG,"wifi正在连接");
                    showProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiActivity2.this);
                    connectType = 2;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType );
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                Log.d(TAG,"网络列表变化了");
                wifiListChange();
            }
        }
    }

    /**
     * //网络状态发生改变 调用此方法！
     */
    public void wifiListChange(){
        sortScaResult();
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this);
        if(connectedWifiInfo != null){
            wifiListSet(connectedWifiInfo.getSSID(),connectType);
        }
    }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     * @param wifiName
     * @param type
     */
    public void wifiListSet(String wifiName , int type){
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if(CollectionUtils.isNullOrEmpty(realWifiList)){
            return;
        }
        for(int i = 0;i < realWifiList.size();i++){
            realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for(int i = 0;i < realWifiList.size();i++){
            WifiBean wifiBean = realWifiList.get(i);
            if(index == -1 && ("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)){
                index = i;
                wifiInfo.setLevel(wifiBean.getLevel());
                wifiInfo.setWifiName(wifiBean.getWifiName());
                wifiInfo.setCapabilities(wifiBean.getCapabilities());
                if(type == 1){
                    wifiInfo.setState(AppContants.WIFI_STATE_CONNECT);
                }else{
                    wifiInfo.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                }
            }
        }
        if(index != -1){
            realWifiList.remove(index);
            realWifiList.add(0, wifiInfo);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查是否已经授予权限
     * @return
     */
    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    public void sortScaResult(){
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(this));
        realWifiList.clear();
        if(!CollectionUtils.isNullOrEmpty(scanResults)){
            for(int i = 0;i < scanResults.size();i++){
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);

                //排序
                Collections.sort(realWifiList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;   //判断用户是否同意获取权限
                    break;
                }
            }

            //如果同意权限
            if (hasAllPermission) {
                mHasPermission = true;
                if(WifiSupport.isOpenWifi(WifiActivity2.this) && mHasPermission){  //如果wifi开关是开 并且 已经获取权限
                    initRecycler();
                }else{
                    Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态或权限获取失败1111",Toast.LENGTH_SHORT).show();
                }

            } else {  //用户不同意权限
                mHasPermission = false;
                Toast.makeText(WifiActivity2.this,"获取权限失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void showProgressBar() {
        pbWifiLoading.setVisibility(View.VISIBLE);
    }

    public void hidingProgressBar() {
        pbWifiLoading.setVisibility(View.GONE);
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
    protected void onDestroy() {
        super.onDestroy();
   /*     if (wifiReceiver != null) {

            BaseActivity.context.unregisterReceiver(wifiReceiver);
            Log.i(TAG, "onDestroy: 注销WiFi监听");
        }*/
    }
}
