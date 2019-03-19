package com.onesmock.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizer;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.serialPort.ConstantValueSerialPort;
import com.onesmock.Util.serialPort.SerialPortActivity;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.activity.main.administratorInformation.wifi.WLANListener;
import com.onesmock.activity.main.administratorInformation.wifi.WifiUtils;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.advertisement.Advertisement;
import com.onesmock.dao.advertisement.AdvertisementDao;
import com.onesmock.dao.author.Author;
import com.onesmock.dao.author.AuthorDao;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.messfromPHP.MessFromPHP;
import com.onesmock.dao.product.Product;
import com.onesmock.dao.product.ProductDao;
import com.onesmock.Util.CloseBarUtil.CloseBarUtil;
import com.onesmock.dao.messfromPHP.MessFromBase;
import com.onesmock.dao.product.ProductMess;
import com.onesmock.dao.product.ProductMessDao;
import com.onesmock.dao.video.Video;
import com.onesmock.dao.video.VideoDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


//import com.onesmock.Util.voiceSpeak.voice.Speek;

//SerialPortActivity

/**
 * 正式项目  需要继承  SerialPortActivity  进行串口数据的交互
 */
public class BaseActivity extends  SerialPortActivity{

    public static WifiUtils mUtils;
    public static WLANListener listener;
   // private static Speek speek;

    public static AuthInfo authInfo = null;
    public static boolean isAythoInfo =false;
    public static SpeechSynthesizer mSpeechSynthesizer = null;
    // =========== 以下为UI部分 ==================================================
    public static Handler mainHandler;

    private static final String TAG = "BaseActivity";
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

    public static String mstrComdata = null;
    public static String orderID = "0";



    public static  boolean isAddFriend = false;
    public static  boolean isBaisuTTs = false;
    public static  boolean isBaisuTTs0 = true;
    public static boolean initBaisuTTs = false;//语音初始化判断
    public static  Bundle bundle;

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
       // speek=new Speek(this);

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

        bundle= new Bundle();
        bundle.putString(ConstantValue.employeeId,"0");
        //获取本机器的相关信息
        //每次开启新的activity都会获取 进行存储放入数据库


        //NetMessFromPhp.getAllMessFromPhp(context);//PHPH


      //  NetMessFromPhp.getJavaAll(context);//JAVA
       // NetMessFromPhp.getVideo(context);
        // 添加Activity到堆栈
        Application.getInstance().addActivity(this);
       // loadingDialog(this, "努力加载中...");

        //C:\Users\zhanghe\Desktop\oneSmock\app\src\main\assets\baiduTTS
    /*    audio= (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        int max =0;
        int current = 0;
        //通话音量
        max = audio.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        current = audio.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);

        //系统音量
        max = audio.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = audio.getStreamVolume( AudioManager.STREAM_SYSTEM );
        Log.d("SYSTEM", "max : " + max + " current : " + current);

        //铃声音量
        max = audio.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = audio.getStreamVolume( AudioManager.STREAM_RING );
        Log.d("RING", "max : " + max + " current : " + current);

        //音乐音量
        max = audio.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = audio.getStreamVolume( AudioManager.STREAM_MUSIC );
        Log.d("MUSIC", "max : " + max + " current : " + current);

        //提示声音音量
        max = audio.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = audio.getStreamVolume( AudioManager.STREAM_ALARM );
        Log.d("ALARM", "max : " + max + " current : " + current);

        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
        audio.setStreamVolume(AudioManager.STREAM_SYSTEM, audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), AudioManager.FLAG_PLAY_SOUND);
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, audio.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_PLAY_SOUND);
        //
        //得到听筒模式的最大值
        audio.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //得到听筒模式的当前值
        audio.getStreamVolume(AudioManager.STREAM_VOICE_CALL);


*/


/*
        // set preference  
        SharedPreferences settings=getSharedPreferences(PREFS_NAME, 0);  
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("silentMode",mSilentMode);
        editor.commit();

      // get preference  
        SharedPreferences settings =getSharedPreferences(PREFS_NAME, 0);  
        boolean silent = settings.getBoolean("silentMode", false);  
*/

    }


    public static void showToastView(String text){
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

    public static boolean isNetworkAvailable(Activity activity) {
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

    //接收数据
    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        mstrComdata = ConstantValueSerialPort.byte2hex(buffer, size);
        Message msg = new Message();
        msg.obj= mstrComdata;

        if(mstrComdata.length()>=40){
            handlerBase_to_Host.sendMessage(msg);
            Log.i(TAG, "onDataReceived: "+mstrComdata);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    BaseActivity.mstrComdata =null;
                    BaseActivity.orderID= "0";
                    Log.i(TAG, "run: 清空BaseActivity.mstrComdata ！");
                }
            },500);

        }
        MessFromPHP.bytes = null;//数据清空 进行数据的修改---确保每次数据都是新的
       //


    }




    //串口返回的消息的处理,发送消息到PHP
    public static Handler handlerBase_to_Host = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mstrComdata = msg.obj.toString();
            MessFromBase messFromBase = com.onesmock.dao.messfromPHP.MessFromBase.getMessFromBase(mstrComdata,context);

            Long l  =byteUtil.parseMd5L16ToLong(messFromBase.getEquipment_host().toLowerCase());
            String a = String.valueOf(l);
            if(a.length()<=17){
                StringBuffer s = new StringBuffer();
                s.append(0);
                s.append(a);
                a=s.toString();
            }
            Log.i(TAG, "handleMessage: "+a);

            messFromBase.setEquipment_host(a);
            l  =byteUtil.parseMd5L16ToLong(messFromBase.getEquipment_base().toLowerCase());
            a=String.valueOf(l);
            if(a.length()<=17){
                StringBuffer s = new StringBuffer();
                s.append(0);
                s.append(a);
                a=s.toString();
            }
            messFromBase.setEquipment_base(a);

            Log.i(TAG, "handleMessage: "+a+"-----------------");
            equipmentDao.dbQueryOneByEquipmentbase(a);
            Log.i(TAG, "handleMessage: ."+ equipmentDao.dbQueryOneByEquipmentbase(a).getEquipmentName());
            BaseActivity.product = productDao.dbQueryOneProduct(a);

            //   Log.i(TAG, "handleMessage: "+MessFromBase.toString());
            //   Log.i(TAG, "handleMessage: "+ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpStockoutUrl);
           //BaseActivity.product=  BaseActivity.productDao.dbQueryOneProduct(messFromBase.getEquipment_base().toLowerCase());

            if(a.length()<=17){
                StringBuffer s = new StringBuffer();
                s.append(0);
                s.append(a);
                a=s.toString();
            }

            Log.i(TAG, "handleMessage: "+messFromBase.getEquipment_base()/*.toLowerCase()*/);
            Log.i(TAG, "handleMessage: "+BaseActivity.product.toString());

            switch (messFromBase.getCheckoutBit()) {//校验位
                case ConstantValue.checkoutBit:
                    switch (messFromBase.getMessageMachineSendToHeader()) {//消息头
                        case ConstantValue.messageMachineSendToHeader:
                            switch (messFromBase.getMesscode()) {//消息
                                case ConstantValue.Shipments://出货成功
                                    Log.i(TAG, "handleMessage: 请稍候，我在快马加鞭哦！");

                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("品尝失败！此货架没有在售产品！请尝试其他货架！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",请您品尝！谢谢惠顾！");

                                                    }
                                                }
                                            }
                                    ).start();


                                    showToastView("请稍候，我在快马加鞭哦！");
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                              //     jsonObject.put(ConstantValue.equipment_host, fromBase.getEquipment_host());
                                               //     jsonObject.put(ConstantValue.equipment_base, fromBase.getEquipment_base());
                                        jsonObject.put(ConstantValue.mess, ConstantValue.Shipments);
                                    } catch (JSONException e) {

                                    }

                                    // 向平台回复消息
                                    // 出货成功
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();

                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successYes)
                                            .add(ConstantValue.orderID,BaseActivity.orderID)//java 平台信息
                                           // .add(ConstantValue.platform, ConstantValue.phpAndroid)// php平台信息
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)//php平台信息
                                           // .add(ConstantValue.fail_reason,"出烟成功！")//php平台信息
                                            .build();
                                    Log.i(TAG, "handleMessage: "+messFromBase.getEquipment_host()+"  "+ messFromBase.getEquipment_base());
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpAcceptDispatchUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaUpdateOrderByOrderId
                                            )//请求的url
                                            .post(formBody)
                                            .build();


                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                            Log.i(TAG, "onFailure:连接失败 ");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);

                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {

                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    break;


                                case ConstantValue.ShipmentsFail://失败出烟
                                    showToastView("出货失败！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("品尝失败！此货架没有在售产品！请尝试其他货架！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",品尝失败！请您重新尝试！谢谢！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    //出烟失败
                                   /* okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successNo)
                                            .add(ConstantValue.orderID,BaseActivity.orderID)//java平台信息

                                           // .add(ConstantValue.platform, ConstantValue.phpAndroid)//php平台信息
                                            //.add(ConstantValue.phpAndroid, ConstantValue.phpVersion)//php平台信息
                                           // .add(ConstantValue.fail_reason, "出烟失败！")//php平台信息
                                            .build();
                                    request = new Request.Builder()
                                            .url(//ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpAcceptDispatchUrl
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaUpdateOrderByOrderId)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                            Log.i(TAG, "onFailure: 连接失败");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });*/
                                    // String srt2=new String(midbytes,"UTF-8");
                                    Log.i(TAG, "出烟失败");
                                    break;



                                case ConstantValue.ShipmentsAuthor://出货成功

                                    showToastView("请稍候，我在快马加鞭哦！员工出货成功！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("此货架没有在售产品！无法出货！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",员工出货成功！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    //Log.i(TAG, mstrComdata.toString());
                                    Log.i(TAG, "handleMessage: 请稍候，我在快马加鞭哦！员工出货成功！");

                                    break;



                                case ConstantValue.ShipmentsAuthorFail://失败出烟
                                    showToastView("员工出货失败！");
                                   // Log.i(TAG, mstrComdata.toString());
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("此货架没有在售产品！无法出货！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",员工出货失败！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 员工出货失败");
                                    break;

                                case ConstantValue.outOfStockYes://缺货     java
                                    Log.i(TAG, "handleMessage: 缺货，请购买其他产品");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("此货架没有在售产品！无法出货！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+"缺货，请购买其他产品！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    //  showToastView("缺货，请购买其他产品！");
                                   // Log.i(TAG, mstrComdata.toString());
                                    //向平台回复消息
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success,ConstantValue.phpIs_successNo)
                                            //.add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .build();
                                    request = new Request.Builder()//lackOfProduct
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpStockoutUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaLackOfProduct)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);
                                                    //上报缺货信息成功    成功后回码给单片机
                                                    StringBuffer stringBuffer= new StringBuffer();
                                                    stringBuffer.append(ConstantValue.messageHeaderSendToMachine);//55  消息头
                                                    stringBuffer.append(ConstantValue.outOfStockYes);//01  消息//01  消息 指令信息
                                                    stringBuffer.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据



                                                    //  String 转为byte   byte转为HEX    随后发送
                                                    long longNum = Long.parseLong( messFromBase.getEquipment_base());
                                                    Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                                                    byte[] long2Bytes = byteUtil.long2Bytes(longNum);
                                                    byteUtil.BinaryToHexString(long2Bytes);
                                                    stringBuffer.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001


                                                    //  String 转为byte   byte转为HEX    随后发送
                                                    longNum = Long.parseLong( messFromBase.getEquipment_host());
                                                    Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                                                    long2Bytes = byteUtil.long2Bytes(longNum);
                                                    byteUtil.BinaryToHexString(long2Bytes);
                                                    stringBuffer.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 00000001

                                                    stringBuffer.append(ConstantValue.checkoutBit);//校验位   55 01 0b 00 00 00 00 00 00 00 01 0000000000000001 00
                                                    if(stringBuffer.toString().length() ==40){
                                                        byte[] bytes = byteUtil.hexStringToByteArray(stringBuffer.toString());
                                                        BaseActivity.SendMessage(bytes);
                                                    }else {

                                                    }

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });

                                    break;

                                case ConstantValue.outOfStockNo://不缺货
                                    // showToastView("不缺货！");
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                           // .add(ConstantValue.platform,ConstantValue.phpAndroid)
                                          //  .add(ConstantValue.phpAndroid,ConstantValue.phpVersion)
                                            .add(ConstantValue.is_success,ConstantValue.phpIs_successYes)
                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpAddGoodsUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaLackOfProduct)//请求的url
                                            .post(formBody)
                                            .build();
                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败!");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {


                                            if (ConstantValue.Okhttp_Back_Success== response.code() ) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                // System.out.println(response.body().string());
                                                JSONObject myJsonObject = null;
                                                JSONObject myJsonObject0 = null;
                                                JSONArray jsonArray;
                                                try {
                                                    myJsonObject = new JSONObject(s);

                                                    int code = myJsonObject.getInt("code");
                                                    String msg = myJsonObject.getString("msg");


                                                    if (ConstantValue.PHP_Back_Success == code) {


                                                        productDao.dbUpdateProductAddTime( equipment.getEquipmentbase());
                                                        Log.i(TAG, "onResponse: 添加货物成功");
                                                        showToastView("添加货物成功!!");

                                                        //上报加货信息成功   成功后回码给单片机
                                                        StringBuffer stringBuffer= new StringBuffer();
                                                        stringBuffer.append(ConstantValue.messageHeaderSendToMachine);//55  消息头
                                                        stringBuffer.append(ConstantValue.outOfStockYes);//01  消息//01  消息 指令信息
                                                        stringBuffer.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据



                                                        //  String 转为byte   byte转为HEX    随后发送
                                                        long longNum = Long.parseLong( messFromBase.getEquipment_base());
                                                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                                                        byte[] long2Bytes = byteUtil.long2Bytes(longNum);
                                                        byteUtil.BinaryToHexString(long2Bytes);
                                                        stringBuffer.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001


                                                        //  String 转为byte   byte转为HEX    随后发送
                                                        longNum = Long.parseLong( messFromBase.getEquipment_host());
                                                        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                                                        long2Bytes = byteUtil.long2Bytes(longNum);
                                                        byteUtil.BinaryToHexString(long2Bytes);
                                                        stringBuffer.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 00000001

                                                        stringBuffer.append(ConstantValue.checkoutBit);//校验位   55 01 0b 00 00 00 00 00 00 00 01 0000000000000001 00
                                                        if(stringBuffer.toString().length() ==40){
                                                            byte[] bytes = byteUtil.hexStringToByteArray(stringBuffer.toString());
                                                            BaseActivity.SendMessage(bytes);
                                                        }else {

                                                        }

                                                    } else if (ConstantValue.PHP_Back_Fail == code) {
                                                        showToastView(msg);
                                                    }

                                                } catch (Exception e) {
                                                }
                                                Log.i(TAG, "onResponse: "+s);
                                            }else {
                                            }
                                        }
                                    });
                                    break;


                                case ConstantValue.portObjects://出口有物体

                                    showToastView("出口有物体！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("此货架没有在售产品！无法出货！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",出口有物体！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 出口有物体！");
                                    // String srt2=new String(midbytes,"UTF-8");
                                  //  Log.i(TAG, mstrComdata.toString());
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                            .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .build();
                                    request = new Request.Builder()
                                            .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.phpForeignUrl)//请求的url
                                            .post(formBody)
                                            .build();
                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });


                                    //出烟失败
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                            .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successNo)
                                            .add(ConstantValue.fail_reason, "出口有物体，出烟失败！")
                                            .build();
                                    request = new Request.Builder()
                                            .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.phpAcceptDispatchUrl)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                            Log.i(TAG, "onFailure: 连接失败");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");
                                    break;


                                case ConstantValue.openTheDoor://开门成功

                                    showToastView("开门成功！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("开门成功！请添加或者更换产品！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",开门成功！请添加或者更换产品！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 开门成功!");
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successYes)//开门
                                            .add(ConstantValue.orderID,BaseActivity.orderID)
                                           // .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                           // .add(ConstantValue.remark,"员工开门!")

                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpOpenDoorUrl*/

                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaRecordMsgOpen)//请求的url
                                            .post(formBody)
                                            .build();
                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");

                                    break;

                                case ConstantValue.openTheDoorFail://失败开门

                                    showToastView("开门失败！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("开门失败！请重新尝试！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",开门失败！请重新尝试！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 开门失败");
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successNo)
                                            .add(ConstantValue.orderID,BaseActivity.orderID)
                                            //.add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpOpenDoorUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaRecordMsgOpen
                                            )//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");

                                    break;


                                case ConstantValue.closeTheDoor://关门成功

                                    showToastView("关门成功！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("关门成功！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",关门成功！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 关门成功");
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                           // .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .add(ConstantValue.orderID,BaseActivity.orderID)
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successYes)
                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpCloseDoorUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaRecordMsgClose)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");

                                    break;

                                case ConstantValue.closeTheDoorFail://失败关门

                                    showToastView("关门失败！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("关门失败！请重新关门");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",关门失败！请重新关门");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 关门失败");
                                    //关门失败
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            //.add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .add(ConstantValue.is_success, ConstantValue.phpIs_successNo)
                                            .add(ConstantValue.orderID,BaseActivity.orderID)

                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpCloseDoorUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaRecordMsgClose)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");

                                    break;

                                case ConstantValue.illegalOPenDoor://非法开门


                                    showToastView("非法开门！");
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(null == BaseActivity.product.getProductName()){
                                                        MainActivity.speak("非法开门！");

                                                    }else{
                                                        MainActivity.speak(BaseActivity.product.getProductName()+",非法开门！");

                                                    }
                                                }
                                            }
                                    ).start();
                                    Log.i(TAG, "handleMessage: 非法开门!");
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.is_success, ConstantValue.illegal)//非法开门
                                            .add(ConstantValue.orderID,BaseActivity.orderID)
                                           // .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                           // .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                           // .add(ConstantValue.is_success, ConstantValue.phpIs_successYes)
                                           // .add(ConstantValue.remark,"非法开门")
                                            .build();
                                    request = new Request.Builder()
                                            .url(/*ConstantValue.httpss+xmppConnect.getPHPIpAddress()+ ConstantValue.phpOpenDoorUrl*/
                                                    ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaRecordMsgClose)//请求的url
                                            .post(formBody)
                                            .build();
                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    // String srt2=new String(midbytes,"UTF-8");

                                    break;

                                case ConstantValue.equipmentFailureOne://故障一

                                    showToastView("设备故障一！");
                                    Log.i(TAG, "handleMessage: 设备故障一");
                                    // String srt2=new String(midbytes,"UTF-8");
                                   // Log.i(TAG, mstrComdata.toString());
                                    //设备故障一
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();

                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                            .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .build();

                                    request = new Request.Builder()
                                            .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.phpEquipmentTroubleUrl)//请求的url
                                            .post(formBody)
                                            .build();
                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    //接收回复的消息
                                    jsonObject = new JSONObject();
                                    try {
                                        //jsonObject.put(ConstantValue.equipment_host, fromBase.getEquipment_host());
                                        jsonObject.put(ConstantValue.equipment_host, messFromBase.getEquipment_base());
                                        jsonObject.put(ConstantValue.mess, ConstantValue.Shipments);
                                    } catch (JSONException e) {

                                    }
                                    break;

                                case ConstantValue.equipmentFailureTwo://故障二

                                    showToastView("设备故障二！");
                                    Log.i(TAG, "handleMessage: 设备故障二");
                                    // String srt2=new String(midbytes,"UTF-8");
                                   // Log.i(TAG, mstrComdata.toString());
                                    //设备故障一
                                    okHttpClient = new OkHttpClient.Builder()
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .build();
                                    //post方式提交的数据
                                    formBody = new FormBody.Builder()
                                            .add(ConstantValue.equipment_host, messFromBase.getEquipment_host())
                                            .add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                            .add(ConstantValue.platform, ConstantValue.phpAndroid)
                                            .add(ConstantValue.phpAndroid, ConstantValue.phpVersion)
                                            .build();

                                    request = new Request.Builder()
                                            .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.phpEquipmentTrouble2Url)//请求的url
                                            .post(formBody)
                                            .build();

                                    //创建/Call
                                    call = okHttpClient.newCall(request);
                                    //加入队列 异步操作
                                    call.enqueue(new okhttp3.Callback() {
                                        //请求错误回调方法
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i(TAG, "onFailure: 连接失败");
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {

                                            if (response.code() == ConstantValue.Okhttp_Back_Success) {
                                                ResponseBody responseBody = response.body();
                                                String s = responseBody.string();
                                                JSONObject myJsonObject = null;
                                                try {
                                                    myJsonObject = new JSONObject(s);
                                                    String code = myJsonObject.getString(ConstantValue.code);
                                                    String msg = myJsonObject.getString(ConstantValue.msg);

                                                } catch (Exception e) {
                                                }
                                                Log.i("okhttp里面的responseBody", s);
                                            }
                                        }
                                    });
                                    //串口信息有返回  则是出货成功，进行计数  并上传数据到平台
                                    break;
                                default:
                                  //  Log.i(TAG, "handleMessage: "+mstrComdata.toString()+"++++++++++++++++++++++++++++++++");

                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };


    //百度语音



    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(/*Context ctx*/) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = BaseActivity.context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(BaseActivity.context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            //LogUtil.d("本软件的版本号：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(/*Context ctx*/) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = BaseActivity.context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(BaseActivity.context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
           // LogUtil.d("本软件的版本名：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}