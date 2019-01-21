package com.onesmock.activity.main.administratorInformation.wifi;

import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

;

import com.onesmock.R;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;

import java.util.List;

/**
 * Search WIFI and show in ListView
 *设置网络 主要是wifi
 */
public class wifiActivity extends BaseActivityO implements OnClickListener,
        OnItemClickListener {
    private Button search_btn;
    private ListView wifi_lv;
    private WifiUtils mUtils;
    private List<String> result;
    private ProgressDialog progressdlg = null;
    private WifiManager mWiFiManager;
    private Button wifi_back_systembase;
    private static final String TAG = "wifiActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        mUtils = new WifiUtils(this);
        //activity 管理
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页


        // 获取WiFi管理者对象
        mWiFiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        if (mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            showToastView("WIFI已经打开，可以使用！！！");

        }else{
            //Toast.makeText(this, "WIFI没有打开，请打开WIFI！！！", Toast.LENGTH_SHORT).show();

            if (!mWiFiManager.isWifiEnabled()) {
                // 打开Wifi连接
                mWiFiManager.setWifiEnabled(true);
                Log.i(TAG, "onCreate: 开启");

            }

        }

        findViews();
        setLiteners();


        wifi_back_systembase= (Button)findViewById(R.id.wifi_back_systembase);

        wifi_back_systembase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getInstance().killActivity(wifiActivity.class);
                openActivity(MainActivity_main_systemCorrelation.class);
            }
        });
    }




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

        }
    }

    /**
     * 开启WiFi
     *
     * @param view
     */
    public void closeWiFi(View view) {
        if (mWiFiManager.isWifiEnabled()) {
            // 关闭Wifi连接
            mWiFiManager.setWifiEnabled(false);
            Log.i(TAG, "closeWiFi: 关闭");


        }
    }

    private void findViews() {
        this.search_btn = (Button) findViewById(R.id.search_btn);
        this.wifi_lv = (ListView) findViewById(R.id.wifi_lv);
    }

    private void setLiteners() {
        search_btn.setOnClickListener(this);
        wifi_lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
            showDialog();
            new MyAsyncTask().execute();
        }
    }

    /**
     * init dialog and show
     */
    private void showDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }

    /**
     * dismiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //扫描附近WIFI信息
            result = mUtils.getScanWifiResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDismiss();
            initListViewData();
        }
    }

    private void initListViewData() {
        if (null != result && result.size() > 0) {
            wifi_lv.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.wifi_list_item,
                    R.id.ssid, result));
        } else {
            wifi_lv.setEmptyView(findViewById(R.layout.list_empty));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tv = (TextView) arg1.findViewById(R.id.ssid);
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            Bundle bundle = new Bundle();
            bundle.putString("ssid", tv.getText().toString());
           // startActivity(in);
            openActivity(WifiConnectActivity.class,bundle);
        }
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
