package com.onesmock.activity.main;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.onesmock.R;
import com.onesmock.Util.FileUtils;
import com.onesmock.Util.banner.Banner;
import com.onesmock.Util.voiceSpeak.control.InitConfig;
import com.onesmock.Util.voiceSpeak.listener.UiMessageListener;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.Application;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.xmpp.MyXMPPTCPConnection;

import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//
// * 首页广告
// * 对广告进行展示以及文字介绍内容
//
//SerialPortActivity
public class MainActivity extends BaseActivity implements OnBannerListener {

    static boolean s= false;
    ////////////
   /* private WifiUtils mUtils;
    private WLANListener listener;*/
    /////////////////
    private static final String TAG = "MainActivity";
    // 广告页面的使用
    //
    private Banner banner;
    private List<String> list;
    private boolean addVideo =true;
    private boolean addImage =true;
    private void initData(){

        list = new ArrayList<>();

        HttpProxyCacheServer proxy = Application.getProxy(getApplicationContext());
       // String proxyUrl = proxy.getProxyUrl("http://www.nj-lsj.net/data/app.mp4");
        List list0= videoDao.dbQueryAll();

        String proxyUrl ;



        Log.i(TAG, "initData: "+list0.size());

        for (int i =0;i<list0.size();i++){
            proxyUrl = proxy.getProxyUrl(list0.get(i).toString());
            Log.i(TAG, "initData: "+list0.get(i).toString());
            list.add(proxyUrl);
        }
       // proxyUrl = proxy.getProxyUrl("http://www.nj-lsj.net/OSS/video/001.mp4");
       // list.add(proxyUrl);
        list.addAll(productDao.dbQueryAllImgurl());
     /*   for (int i =0;i<productDao.dbQueryAllImgurl().size();i++){
            proxyUrl = proxy.getProxyUrl(productDao.dbQueryAllImgurl().get(i).toString());
            list.add(proxyUrl);
        }*/

/*        String proxyUrl2 = proxy.getProxyUrl("http://www.nj-lsj.net/data/1.swf");
        String proxyUrl3 = proxy.getProxyUrl("http://www.nj-lsj.net/data/2.avi");
        String proxyUrl4 = proxy.getProxyUrl("http://www.nj-lsj.net/data/3.mkv");
        String proxyUrl5 = proxy.getProxyUrl("http://www.nj-lsj.net/data/4.flv");
        String proxyUrl6 = proxy.getProxyUrl("http://www.nj-lsj.net/data/5.mpg");
        String proxyUrl7 = proxy.getProxyUrl("http://www.nj-lsj.net/data/6.wmv");*/




       // list.add(proxyUrl);
      //  list.add(proxyUrl2);
      //  list.add(proxyUrl3);
     //   list.add(proxyUrl4);
     //   list.add(proxyUrl5);
      //  list.add(proxyUrl6);
      //  list.add(proxyUrl7);
       // List list1= productDao.dbQueryAllImgurl();

       // list.add(list1.get(0).toString());
     //   Log.i(TAG, "initData: "+list1.get(0).toString());
       // Log.i(TAG, "initData: "+list0.get(0).toString());

        // list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
/*        list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");*/
    }



    /*public void onClick(View view){
        if (view.getId() == R.id.bt){
            HttpProxyCacheServer proxy = Application.getProxy(getApplicationContext());
            String proxyUrl = proxy.getProxyUrl("http://www.nj-lsj.net/video/app.mp4");
            String proxyUrl2 = proxy.getProxyUrl("http://www.nj-lsj.net/video/app.mp4");

            list = new ArrayList<>();
            list.add(proxyUrl2);
            list.add("http://img1.imgtn.bdimg.com/it/u=344091145,309580146&fm=27&gp=0.jpg");
            list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
            list.add(proxyUrl);
            list.add("http://img3.imgtn.bdimg.com/it/u=1399356699,3785361628&fm=27&gp=0.jpg");
        }else {
            list = new ArrayList<>();
        }
        banner.dataChange(list);
    }*/
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    // 实现openfire
    public static MyXMPPTCPConnection m_con = null;
    //按钮设定
    private static final int REQUEST_SET_SYSTEM = 3;
    //二维码
    public static ImageView TwoDimensionalCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//加载进来
        Log.i(TAG, "开机自启动处理");
        //activity管理信息


        AppManager.getInstance().addActivity(this);
        FileUtils.getInstance(BaseActivity.context).copyAssetsToSD("baiduTTS", "/baiduTTS");//复制百度的资源到Sdcard下面

        //56111100470ECDB4271001002387DB4466100200

        isBaisuTTs = BaseActivity.isNetworkAvailable(MainActivity.this);//是否联网
        //地址的变动
        initData();
        initView();
        initPermission();





        String printTxtPath = getApplicationContext().getPackageResourcePath() + "/files/" ;
        // /data/app/com.example.fileoperation-2.apk/files/printMenu.txt

        String printTxtPath0 = getApplicationContext().getFilesDir().getPath();
        String printTxtPath1= getApplicationContext().getFilesDir().getAbsolutePath();
         // /data/data/com.example.fileoperation/files
        //获取当前程序路径
        getApplicationContext().getFilesDir().getAbsolutePath();
        //获取该程序的安装包路径
        String path=getApplicationContext().getPackageResourcePath();
        Log.i(TAG, "onCreate: "+printTxtPath+"--"+printTxtPath0+"--"+printTxtPath1+"--"+path);
        //获取程序默认数据库路径




        Log.i(TAG, "onCreate: "+getLocalVersionName()+"-----------------------"+getLocalVersion());

        new Thread(new Runnable() {
            @Override
            public void run() {
                initTTs();
            }
        });

        TwoDimensionalCode = (ImageView) findViewById(R.id.TwoDimensionalCode);
        //首页二维码图片



        if (/*!MyXMPPTCPConnection.IsOnline && b && */!xmppConnect.binitOpenfire) {
            Log.i(TAG, "--------------------" + MyXMPPTCPConnection.IsOnline + xmppConnect.binitOpenfire);
            xmppConnect.binitOpenfire = true;
            new Thread(new Runnable() {
                public void run() {

                    xmppConnect.xmppLogin(xmppConnect.getuserName());
                }
            }).start();
        }
        ;

        if (BaseActivity.isAddFriend) {
            //定时器之启动一次
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg1 = Message.obtain();
                    xmppConnect.mAddFriend.sendMessage(msg1);

                    if(BaseActivity.isNetworkAvailable(MainActivity.this)){

                        new Timer().schedule(new TimerTask() {
                            //
                            @Override
                            public void run() {

                                if(/*BaseActivity.isNetworkAvailable(MainActivity.this)&&*/initBaisuTTs){

                                    Log.i(TAG, "run: 不需要初始化网络信息");
                                }else{
                                    MainActivity.initTTs();
                                    Log.i(TAG, "run: 判断网络，延迟初始化语音信息");
                                }

                            }
                        },  10*1000);
                    }else{
                        Log.i(TAG, "run: 没有网络，无法初始化语音信息！");
                    }

                }
            }, 10 * 1000, 30 * 1000);
            isAddFriend = true;
        }


        File f = new File( xmppConnect.FILE_PATH +"/app.JEPG");
        //二维码信息
        if(f.exists()){
            Message message = Message.obtain();
            message.what = ConstantValue.GetBitmapTwoDimensionalCode;
            message.obj = ConstantValue.bitmapTwoDimensionalCode;
            handlerImg.sendMessage(message);
            Log.i(TAG, "run: 设置二维码信息1");
        }else{

        }

    }

    public static void getTwoDimensionalCode(){//获取二维码

        String equipmentID = equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmenthost();
        Log.i(TAG, "getTwoDimensionalCode: "+equipmentID);


                    try {
                        OkHttpClient client = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaGetAccessToken)
                                .build();
                        Log.i(TAG, "onCreate: " +ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.javaGetAccessToken);
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());

                                    String key = jsonObject.getString("msg");
                                    Log.i(TAG, "onResponse: "+key);
                                    Message message = new Message();
                                    message.what = ConstantValue.GetBitmapTwoDimensionalCode;
                                    message.obj = xmppConnect.getImageFromNetWork(ConstantValue.GetBitmapTwoDimensionalCodeAddress
                                                    + key,
                                            "{\"page\":\"pages/index/index\",\"scene\":\""+equipmentID+"\",\"width\":5}");
                                    handlerImg.sendMessage(message);

                                }catch (Exception e){
                                }
                                Log.i(TAG, "onResponse: 更新UI");
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


    }




    public  static Handler handlerImg0 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantValue.XMPP_MSG_CONNECT_INFO:
                    xmppConnect.xmppLogin(xmppConnect.getuserName());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                    }
                    });
                    MainActivity.initTTs();

                break;

            }


        }
    };



    //二维码图片设置
    //wifi状态检测
    public static Handler handlerImg = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValue.GetBitmapTwoDimensionalCode:
                    ConstantValue.bitmapTwoDimensionalCode = (Bitmap)msg.obj;
                    TwoDimensionalCode.setImageBitmap(ConstantValue.bitmapTwoDimensionalCode);
                    Log.i(TAG, "handleMessage: 图片二维码");
                    break;


                case ConstantValue.GetBitmapTwoDimensionalCode99:
                    byte[] obj =(byte[])msg.obj;
                    ConstantValue.bitmapTwoDimensionalCode = BitmapFactory.decodeByteArray(obj, 0, obj.length);
                    TwoDimensionalCode.setImageBitmap(ConstantValue.bitmapTwoDimensionalCode);
                    Log.i(TAG, "handleMessage:放入图片 "+ obj.length);

                    break;

               /* case ConstantValue.MSG_WIFI_MESS:

                    listener = new WLANListener(context);
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
                            if (!mUtils.isConnectWifi()) {
                                new MyAsyncTask().execute();
                            } else {
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
                    if (!mUtils.isWifiOPened()) {
                        mUtils.EnableWifi();
                    }

                    break;*/


                case ConstantValue.MSG_LOAD_IMAGE:
                    // Bitmap bitmap = (Bitmap) msg.obj;
                    //  imageView2.setImageBitmap(bitmap);
                    break;

            }
        }
    };

    //用于跳转下一画面
    public void attivity_main_main_to_1(View view) {
        openActivity(MainActivity_main.class);

    }

    //用于跳转下一画面
    public void attivity_main_main_to_2(View view) {
        openActivity(MainActivity_main.class);
    }

    //
    //分机器消息，接收处理
    //货架返回处理信息给  主机  主机解析

    ////////////////*******************xmpp***********************
    @Override
    protected void onDestroy() {
   /*     if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
           // print("释放资源成功");
            Log.i(TAG, "onDestroy: 释放资源成功");
        }
*/
        banner.destroy();
        if (null != m_con) {
            m_con.disconnect();
        }
        super.onDestroy();
    }

    ////////////////*******************xmpp***********************
    ////////////**************短线重新链接a

    //广告信息处理  首页轮播广告  从数据库读取的图片链接地址
    private void initView() {
        banner = (Banner) findViewById(R.id.banner);

        banner.setDataList(list);
        banner.setImgDelyed(5000);
        banner.startBanner();
        banner.startAutoPlay();
//        //放图片地址的集合
//        list_path = new ArrayList<>();
//        //放标题的集合
//        list_title = new ArrayList<>();
//
//
//        list_path = productDao.dbQueryAllImgurl();
//        list_path.addAll(advertisementDao.dbQueryAll());
//        list_title = productDao.dbQueryAllProductname();
//        for (int i = 0; i < list_path.size(); i++) {
//            Log.i(TAG, "initView: " + list_path.get(i) + "");
//        }
////       list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        //    list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        //     list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        //     list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
//        //   list_title.add("好好学习");
//        //   list_title.add("天天向上");
//        //   list_title.add("热爱劳动");
//        //    list_title.add("不搞对象");*/
//        //设置内置样式，共有六种可以点入方法内逐一体验使用。
//
//        banner.setBannerStyle(BannerConfig.CENTER);
//        //设置图片加载器，图片加载器在下方
//        banner.setImageLoader(new MyLoader());
//        //设置图片网址或地址的集合
//        banner.setImages(list_path);
//        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
//        banner.setBannerAnimation(Transformer.BackgroundToForeground);
//        //设置轮播图的标题集合
//        // banner.setBannerTitles(list_title);
//        //设置轮播间隔时间
//        banner.setDelayTime(4000);
//        //设置是否为自动轮播，默认是“是”。
//        banner.isAutoPlay(true);
//        //设置指示器的位置，小点点，左中右。
//        banner.setIndicatorGravity(BannerConfig.CENTER)
//                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                .setOnBannerListener(this)
//                //必须最后调用的方法，启动轮播图。
//                .start();
//


        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                   // print(msg.obj.toString());
                    Log.i(TAG, "handleMessage: "+msg.obj.toString());
                }
            }

        };
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i(TAG, "OnBannerClick:你点了第" + position + "张轮播图");
        Intent setSystemIntent = new Intent(MainActivity.this, MainActivity_main.class);
        setSystemIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(setSystemIntent, REQUEST_SET_SYSTEM);

    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
         /*   Glide.with(context)
                    .asBitmap()
                    .load((String) path).*/
          //  Glide.with(context).load((String)path).
        }
    }


    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);

       /* if(!isBaisuTTs && isBaisuTTs0){
            Message message1 = Message.obtain();
            message1.what=ConstantValue.XMPP_MSG_CHAT_CALLBACK;
            handlerImg0.sendMessage(message1);
            isBaisuTTs0 =false;

        }*/
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
        if (!mUtils.isConnectWifi()) {
            SystemValuesDao systemValuesDao = new SystemValuesDao(context);
            SystemValues systemValues = systemValuesDao.dbQueryOneByID(ConstantValue.codeValue0);
            mUtils.connectWifiTest(systemValues.getName(), systemValues.getValue());
            Log.i(TAG, "initListViewData: " + systemValues.getValue() + systemValues.getName());
        }
       /* Message msg = Message.obtain();
        msg.what = MSG_LOAD_GRID;
        mHandler.sendMessage(msg);*/

    }





    /**
     * 注意此处为了说明流程，故意在UI线程中调用。
     * 实际集成中，该方法一定在新线程中调用，并且该线程不能结束。具体可以参考NonBlockSyntherizer的写法
     */
    public static void initTTs() {

        LoggerProxy.printable(true); // 日志打印在logcat中
        boolean isMix = ConstantValue.baiduTtsMode.equals(TtsMode.MIX);
        boolean isSuccess;
        if (isMix) {
            // 检查2个离线资源是否可读
            isSuccess = checkOfflineResources();
            if (!isSuccess) {
                return;
            } else {
             //   print("离线资源存在并且可读, 目录：" + ConstantValue.baiduTEMP_DIR);
                Log.i(TAG, "initTTs:离线资源存在并且可读, 目录：" + ConstantValue.baiduTEMP_DIR);
            }
        }
        LoggerProxy.printable(true);
        // 日志更新在UI中，可以换成MessageListener，在logcat中查看日志
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        // 1. 获取实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(BaseActivity.context);

        // 2. 设置listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);

        // 3. 设置appId，appKey.secretKey
        int result = mSpeechSynthesizer.setAppId(ConstantValue.baiduAppId);
        checkResult(result, "setAppId");
        result = mSpeechSynthesizer.setApiKey(ConstantValue.baiduAppKey, ConstantValue.baiduSecretKey);
        checkResult(result, "setApiKey");

        // 4. 支持离线的话，需要设置离线模型
        if (isMix) {
            // 检查离线授权文件是否下载成功，离线授权文件联网时SDK自动下载管理，有效期3年，3年后的最后一个月自动更新。
            isSuccess = checkAuth();

      /*      if(BaseActivity.isAythoInfo){
            }else{
                authInfo = mSpeechSynthesizer.auth(ConstantValue.baiduTtsMode);
            }
*/
            if (!isSuccess) {
                return;
            }

            // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ConstantValue.baiduTEXT_FILENAME);
            // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ConstantValue.baiduMODEL_FILENAME);
        }

        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);

        // x. 额外 ： 自动so文件是否复制正确及上面设置的参数
        Map<String, String> params = new HashMap<>();
        // 复制下上面的 mSpeechSynthesizer.setParam参数
        // 上线时请删除AutoCheck的调用
        if (isMix) {
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ConstantValue.baiduTEXT_FILENAME);
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ConstantValue.baiduMODEL_FILENAME);
        }
        InitConfig initConfig =  new InitConfig(ConstantValue.baiduAppId, ConstantValue.baiduAppKey, ConstantValue.baiduSecretKey, ConstantValue.baiduTtsMode, params, listener);
      /*  AutoCheck.getInstance(BaseActivity.context).check(initConfig, new Handler() {
            @Override
              //开新线程检查，成功后回调
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        //print(message); // 可以用下面一行替代，在logcat中查看代码
                        Log.i(TAG, "handleMessage: "+message);
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
*/
        // 6. 初始化
        result = mSpeechSynthesizer.initTts(ConstantValue.baiduTtsMode);
        checkResult(result, "initTts");
        initBaisuTTs= true;
    }

    /**
     * 检查appId ak sk 是否填写正确，另外检查官网应用内设置的包名是否与运行时的包名一致。本demo的包名定义在build.gradle文件中
     *
     * @return
     */
    private static boolean checkAuth() {

        authInfo = mSpeechSynthesizer.auth(ConstantValue.baiduTtsMode);
        if (!authInfo.isSuccess()) {
            // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
            String errorMsg = authInfo.getTtsError().getDetailMessage();
          //  print("【error】鉴权失败 errorMsg=" + errorMsg);
            Log.i(TAG, "checkAuth: 【error】鉴权失败 errorMsg=" + errorMsg);
            return false;
        } else {
           //print("验证通过，离线正式授权文件存在。");
            Log.i(TAG, "checkAuth: 验证通过，离线正式授权文件存在。");
            BaseActivity.isAythoInfo = true;
            return true;
        }
    }

    /**
     * 检查 TEXT_FILENAME, MODEL_FILENAME 这2个文件是否存在，不存在请自行从assets目录里手动复制
     *
     * @return
     */
    private static boolean checkOfflineResources() {
        String[] filenames = {ConstantValue.baiduTEXT_FILENAME, ConstantValue.baiduMODEL_FILENAME};
        for (String path : filenames) {
            File f = new File(path);
            if (!f.canRead()) {
               // print("[ERROR] 文件不存在或者不可读取，请从assets目录复制同名文件到：" + path);
                Log.i(TAG, "checkOfflineResources: [ERROR] 文件不存在或者不可读取，请从assets目录复制同名文件到：" + path);
                Log.i(TAG, "checkOfflineResources: [ERROR] 初始化失败！！！");
               // print("[ERROR] 初始化失败！！！");
                return false;
            }
        }
        return true;
    }

    public static void speak(String text) {

        /* 以下参数每次合成时都可以修改
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
         *  设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5"); 设置合成的音量，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5"); 设置合成的语速，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5"); 设置合成的语调，0-9 ，默认 5
         *
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
         *  MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         */

        if (mSpeechSynthesizer == null) {
           // print("[ERROR], 初始化失败");
            Log.i(TAG, "speak: 初始化失败");
   /*         new Thread(new Runnable() {
                @Override
                public void run() {
                    initTTs();
                }
            });*/
            return;
        }/*else{
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "4");
            // 设置合成的音量，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
            // 设置合成的语速，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
            // 设置合成的语调，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        }*/

        int result = mSpeechSynthesizer.speak(text);
        //mShowText.setText("");
       // print("合成并播放 按钮已经点击");
        Log.i(TAG, "speak: 合成并播放 按钮已经点击");
        checkResult(result, "speak");
    }

    private void stop() {
        //print("停止合成引擎 按钮已经点击");
        Log.i(TAG, "stop: 停止合成引擎 按钮已经点击");
        int result = mSpeechSynthesizer.stop();
        checkResult(result, "stop");
    }

    //  下面是UI部分

  /*  private void initView() {
        mSpeak = (Button) this.findViewById(R.id.speak);
        mStop = (Button) this.findViewById(R.id.stop);
        mShowText = (TextView) this.findViewById(R.id.showText);
        mShowText.setText(DESC);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.speak:
                        speak();
                        break;
                    case R.id.stop:
                        stop();
                        break;
                    default:
                        break;
                }
            }
        };
        mSpeak.setOnClickListener(listener);
        mStop.setOnClickListener(listener);

    }*/

    private static void print(String message) {
        Log.i(TAG, message);
        //mShowText.append(message + "\n");
    }



    private static void checkResult(int result, String method) {
        if (result != 0) {
           // print("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Log.i(TAG, "checkResult: error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    //  下面是android 6.0以上的动态授权

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

       // Message message = Message.obtain();
        //handlerImg0.sendMessage(message);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
}
