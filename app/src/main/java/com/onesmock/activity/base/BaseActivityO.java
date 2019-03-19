package com.onesmock.activity.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.onesmock.Util.CloseBarUtil.CloseBarUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.serialPort.SerialPortActivity;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.main.administratorInformation.wifi.WLANListener;
import com.onesmock.activity.main.administratorInformation.wifi.WifiUtils;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.advertisement.Advertisement;
import com.onesmock.dao.advertisement.AdvertisementDao;
import com.onesmock.dao.author.Author;
import com.onesmock.dao.author.AuthorDao;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.product.Product;
import com.onesmock.dao.product.ProductDao;
import com.onesmock.dao.product.ProductMess;
import com.onesmock.dao.product.ProductMessDao;
import com.onesmock.dao.video.Video;
import com.onesmock.dao.video.VideoDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BaseActivityO extends Activity {

    public static WifiUtils mUtils;
    public static WLANListener listener;

    private static final String TAG = "BaseActivityO";
    public static SystemValuesDao systemValuesDao;
    public static AuthorDao authorDao;
    public static EquipmentDao equipmentDao;
    public static ProductDao productDao;
    public static Context context;
    public static BackMain backMain;
    public static AdvertisementDao advertisementDao;
    public static VideoDao videoDao;
    public static ProductMessDao productMessDao;

    public static SystemValues systemValues;
    public static Equipment equipment;
    public static Product product;
    public static Author author;
    public static Toast toast;
    public static TextView view;
    public static Advertisement advertisement;
    public static Video video;
    public static ProductMess productMess;
    /**
     * 网络连接 okhttp相关
     *
     * @param savedInstanceState
     */
    public static OkHttpClient okHttpClient;
    public static FormBody formBody;
    public static Call call;
    public static Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //去除title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置屏幕长亮
        //       设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        hideBottomUIMenu();//隐藏标题
        //关闭底部导航条
        CloseBarUtil.closeBar();

        context = this;

        systemValuesDao = new SystemValuesDao(context);
        authorDao = new AuthorDao(context);
        equipmentDao = new EquipmentDao(context);
        productDao = new ProductDao(context);
        advertisementDao = new AdvertisementDao(context);
        videoDao = new VideoDao(context);
        productMessDao = new ProductMessDao(context);



        systemValues = new SystemValues();
        equipment = new Equipment();
        product = new Product();
        author = new Author();
        advertisement = new Advertisement();
        video = new Video();
        productMess = new ProductMess();

        //获取本机器的相关信息
        //每次开启新的activity都会获取 进行存储放入数据库
       //NetMessFromPhp.getAllMessFromPhp(context);
        // 添加Activity到堆栈
        Application.getInstance().addActivity(this);

        // loadingDialog(this, "努力加载中...");


    }


    public void showToastView(String text){
        toast = new Toast(context);
        view = new TextView(context);
        view.setText(text);
        view.setTextSize(30);
        view.setBackgroundResource(android.R.color.holo_green_light);
        view.setTextColor(Color.RED);
        //view.getBackground().setAlpha(12);//背景透明

        view.setPadding(10, 10, 10, 10);
        toast.setGravity(Gravity.CENTER, 0, 40);
        toast.setView(view);
        toast.show();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 自定义dialog
     **/
    //protected Dialog dialog;

    /**
     * 自定义dialog
     *
     * @param context
     * @param dialogString
     */
    // protected void loadingDialog(Context context, String dialogString) {
      /*  dialog = new Dialog(context, R.style.new_circle_progress);
        dialog.setContentView(R.layout.layout_progressbar);
        ((TextView) dialog.findViewById(R.id.emptyView)).setText(dialogString);// dialog显示时的字样*/
    // }


    /**
     * 简化findViewById()
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T mFindViewById(int id) {
        // return 返回view时，加上泛型T
        return (T) findViewById(id);

    }

    /**
     * 当前Activity已onStop后(未销毁),重新再次进入后来调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 在onCreate()和onRestart()之后回调
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 在onStart()之后来调用 (获取到焦点,进入用户可操作界面)
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 当前Activity失去焦点后来调用
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 当前Activity不再可见后将来调用 (在onPausse之后)
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 当前Activity被销毁来调用 ( android.app.Activity.finish() )
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

       // Application.getInstance().removeActivity(this);

    }

    /**
     * 某个activity变得“容易”被系统销毁时，该activity的onSaveInstanceState就会被执行，
     * 除非该activity是被用户主动销毁的。
     * (按下HOME键？长按HOME键，选择运行其他的程序?按下电源按键?activity切换？屏幕方向切换？)
     * 此方法常常用来做一些应用中非持久性的存储
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 该activity被恢复时执行(前提是该activity的确已被销毁,即此方法与onSaveInstanceState()方法不一定对等成对调用)
     * ,且savedInstanceState参数还会传递到onCreate()内。 此方法常常用来做一些应用中非持久性的存储的恢复。
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 此方法与清单中android:configChanges对等调用(不指定,配置改变时将重调用onCreate方法,指定后,
     * 指定情况下将不调用onCreate方法而来调用此方法,故你懂的啦)
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object onRetainNonConfigurationInstance() {
        return super.onRetainNonConfigurationInstance();
    }

    /**
     * Activity跳转
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * Activity跳转
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, null);
    }

    /**
     * Activity跳转
     *
     * @param pClass
     * @param pBundle
     * @param uri
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        openActivity(pAction, pBundle, null);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     * @param pBundle
     * @param uri
     */
    protected void openActivity(String pAction, Bundle pBundle, Uri uri) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /**
     * intent
     *
     * @return
     */
    protected Intent getPrevIntent() {
        return getIntent();
    }

    /**
     * bundle
     *
     * @return
     */
    protected Bundle getPrevExtras() {
        return getPrevIntent().getExtras();
    }

    /**
     * 关闭应用
     */
    public void finishDefault() {
        finish();
    }


    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    protected boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}


