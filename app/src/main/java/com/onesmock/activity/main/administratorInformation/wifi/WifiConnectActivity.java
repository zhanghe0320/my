package com.onesmock.activity.main.administratorInformation.wifi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onesmock.R;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity;

/**
 * 连接指定的WIFI
 *
 */
public class WifiConnectActivity extends BaseActivityO implements OnClickListener {
	private Button connect_btn;
	private TextView wifi_ssid_tv;
	private EditText wifi_pwd_tv;
	private WifiUtils mUtils;
	// wifi之ssid
	private String ssid;
	private String pwd;
	private ProgressDialog progressdlg = null;
	private static final String TAG = "WifiConnectActivity";

	private Button connect_back_wifiactivity;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				if(/*BaseActivity.isNetworkAvailable(MainActivity.this)&&*/BaseActivity.initBaisuTTs){

					Log.i(TAG, "run: 不需要初始化网络信息");
				}else{
					MainActivity.initTTs();
					Log.i(TAG, "run: 判断网络，延迟初始化语音信息");
				}

				showToastView("WIFI连接成功!");
				break;
			case 1:
				showToastView("WIFI连接失败!");
				break;
			}
			progressDismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		mUtils = new WifiUtils(this);
		//activity 管理
		AppManager.getInstance().addActivity(this);
		backMain = new BackMain(1000 * 30, 1000, this);    //region 无操作 返回主页


		connect_back_wifiactivity= (Button) findViewById(R.id.connect_back_wifiactivity) ;
		connect_back_wifiactivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AppManager.getInstance().killActivity(WifiConnectActivity.class);
				openActivity(wifiActivity.class);
			}
		});
		findViews();
		setLiteners();
		initDatas();
	}

	/**
	 * init dialog
	 */
	private void progressDialog() {
		progressdlg = new ProgressDialog(this);
		progressdlg.setCanceledOnTouchOutside(false);
		progressdlg.setCancelable(false);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(getString(R.string.wait_moment));
		progressdlg.show();
	}

	/**
	 * dissmiss dialog
	 */
	private void progressDismiss() {
		if (progressdlg != null) {
			progressdlg.dismiss();
		}
	}

	private void initDatas() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ssid = bundle.getString("ssid");
		if (!TextUtils.isEmpty(ssid)) {
			ssid = ssid.replace("\"", "");
		}
		this.wifi_ssid_tv.setText(ssid);
	}

	private void findViews() {
		this.connect_btn = (Button) findViewById(R.id.connect_btn);
		this.wifi_ssid_tv = (TextView) findViewById(R.id.wifi_ssid_tv);
		this.wifi_pwd_tv = (EditText) findViewById(R.id.wifi_pwd_tv);
	}

	private void setLiteners() {
		connect_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.connect_btn) {// 下一步操作
			pwd = wifi_pwd_tv.getText().toString();
			// 判断密码输入情况
			if (TextUtils.isEmpty(pwd)) {
				showToastView("请输入wifi密码");
				return;
			}

		/*	new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						Log.w(TAG, "run: root Runtime->reboot");//重启设备
						Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","reboot "});
						proc.waitFor();
					}catch (Exception ex){
						ex.printStackTrace();
					}
					final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());//重启app
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					//MainActivity.TwoDimensionalCode.setImageBitmap(ConstantValue.bitmapTwoDimensionalCode);

				}
			},30*1000);*/



			progressDialog();
			// 在子线程中处理各种业务
			dealWithConnect(ssid, pwd);


			systemValuesDao.dbInsert(ssid,pwd,systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getEquipmenthost(),ConstantValue.codeValue0);

			Message message = Message.obtain();
			message.what= ConstantValue.XMPP_MSG_CONNECT_INFO;
			MainActivity.handlerImg0.sendMessage(message);


		}
	}

	private void dealWithConnect(final String ssid, final String pwd) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				// 检验密码输入是否正确
				boolean pwdSucess = mUtils.connectWifiTest(ssid, pwd);
				try {
					Thread.sleep(4000);

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (pwdSucess) {

					mHandler.sendEmptyMessage(0);

				} else {
					mHandler.sendEmptyMessage(1);
				}
			}
		}).start();
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
