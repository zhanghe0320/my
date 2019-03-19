package com.onesmock.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.floatball.FloatBallCfg;
import com.huxq17.floatball.libarary.menu.FloatMenuCfg;
import com.huxq17.floatball.libarary.menu.MenuItem;
import com.huxq17.floatball.libarary.utils.BackGroudSeletor;
import com.huxq17.floatball.libarary.utils.DensityUtil;
import com.onesmock.R;
import com.onesmock.Util.bitmapCache.MyBitmapUtils;
import com.onesmock.Util.bitmapCache.mGlideUrl;
import com.onesmock.Util.dialog.ConfirmDialogEditext;
import com.onesmock.Util.productList.GridAdapter;
import com.onesmock.Util.productList.ImageInfo;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.manufactor.manufactorSetActivity;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.product.Product;
import com.onesmock.dao.product.ProductDao;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.serialPort.ConstantValueSerialPort;
import com.onesmock.xmpp.SendMessageToPhp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 主程序入口 展示所有产品的信息
 * 提供进入app管理员界面的入口
 * 拥有定时器  超过一定时间不操作 就会退回到广告界面
 *
 */
//SerialPort
public class MainActivity_main extends BaseActivityO /*implements View.OnClickListener */{

    //悬浮窗口
    private FloatBallManager mFloatballManager;

    private static final String TAG = "MainActivity_main";
    public void showFloatBall(View v) {
//        mFloatballManager.show();
        setFullScreen(v);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //只有activity被添加到windowmanager上以后才可以调用show方法。
        mFloatballManager.show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFloatballManager.hide();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    private void exitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        isfull = false;
    }

    private boolean isfull = false;

    public void setFullScreen(View view) {
        if (isfull == true) {
            exitFullScreen();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isfull = true;
        }
    }

    private void initSinglePageFloatball(boolean showMenu) {
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        int ballSize = DensityUtil.dip2px(this, 80);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_BOTTOM);
        //设置悬浮球不半隐藏
        //        ballCfg.setHideHalfLater(false);



        if (showMenu) {
            //2 需要显示悬浮菜单
            //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
            int menuSize = DensityUtil.dip2px(this, 200);
            int menuItemSize = DensityUtil.dip2px(this, 80);
            FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
            //3 生成floatballManager
            //必须传入Activity
            mFloatballManager = new FloatBallManager(this, ballCfg, menuCfg);
            addFloatMenuItem();
        } else {
            //必须传入Activity
            mFloatballManager = new FloatBallManager(this, ballCfg);
        }
    }


    private void addFloatMenuItem() {
        MenuItem  authorItem = new MenuItem(BackGroudSeletor.getdrawble("ic_author", this)) {
            @Override
            public void action() {
                Log.i(TAG, "action: 管理员设置");

                //系统设置按钮
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_main.this).create();
                alertDialog.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                final Window window = alertDialog.getWindow();


                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                hideBottomUIMenu();

                window.setContentView(R.layout.activity_login);
                final EditText tv_InputPwd = (EditText) window.findViewById(R.id.editInputPwd);
                final EditText tv_InputName = (EditText) window.findViewById(R.id.editInputName);

                Button btn_pwdOk = (Button) window.findViewById(R.id.btnPwdOK);
                Button btn_cancle = (Button) window.findViewById(R.id.btnCancle);
                //确认
                btn_pwdOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                        String employeeId= tv_InputName.getText().toString();
                        String passWord= tv_InputPwd.getText().toString();

                        String pwd2 = systemValuesDao.dbQueryOneByName(ConstantValue.manufactorPassword).getValue();
                        String pwd1 = systemValuesDao.dbQueryOneByName(ConstantValue.authorPassword).getValue();

                        String strPwd = new String().format("%s", tv_InputPwd.getText());
                        String strName = new String().format("%s", tv_InputName.getText());

                        if ((strPwd.compareTo(pwd2) == 0 && strName.compareTo("njlsj")==0)||(strPwd.compareTo(pwd1) == 0 && strName.compareTo("njlsj")==0)) {
                            //进入系统设置页面
                            openActivity(MainActivity_main_systemCorrelation.class);
                            AppManager.getInstance().killActivity(MainActivity_main.class);
                            //startActivity(intent);
                            alertDialog.dismiss();
                            mFloatballManager.closeMenu();
                        } else {
                            okHttpClient = new OkHttpClient.Builder()
                                    .connectTimeout(10, TimeUnit.SECONDS)
                                    .writeTimeout(10, TimeUnit.SECONDS)
                                    .readTimeout(20, TimeUnit.SECONDS)
                                    .build();
                            //post方式提交的数据
                            formBody = new FormBody.Builder()
                                    .add(ConstantValue.equipment_host, xmppConnect.getuserName())
                                    //.add(ConstantValue.equipment_base, messFromBase.getEquipment_base())
                                    .add(ConstantValue.employeeId, employeeId)
                                    .add(ConstantValue.passWord,passWord)
                                    .build();
                            Log.i(TAG, "onClick: "+employeeId+"------------------"+passWord);
                            request = new Request.Builder()
                                    .url(ConstantValue.https+xmppConnect.getPHPIpAddress()+ ConstantValue.javaMechineLogin)//请求的url
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
                                   /* JSONObject myJsonObject = null;
                                    try {
                                        myJsonObject = new JSONObject(s);
                                        String code = myJsonObject.getString(ConstantValue.code);
                                        String msg = myJsonObject.getString(ConstantValue.msg);

                                    } catch (Exception e) {
                                    }*/
                                        Log.i("okhttp里面的responseBody", s);


                                        if (s.equals("\"success\"") ) {
                                            //进入系统设置页面
                                            openActivity(MainActivity_main_systemCorrelation.class);
                                            BaseActivity.bundle.putString(ConstantValue.employeeId,employeeId);
                                            //获取数据
                                            //  Intent intent=getIntent();
                                            //从intent取出bundle
                                            // Bundle bundle=intent.getBundleExtra("data");
                                            //获取数据
                                            // String name=bundle.getString("name");
                                            // String num=bundle.getString("number");
                                            // openActivity(MainActivity_main_systemCorrelation.class);
                                            AppManager.getInstance().killActivity(MainActivity_main.class);
                                            //startActivity(intent);
                                            alertDialog.dismiss();
                                            mFloatballManager.closeMenu();

                                        } else {
                                            Message msg = Message.obtain();
                                            msg.what = ConstantValue.GetBitmapTwoDimensionalCode99;
                                            msg.obj = "密码错误！请核对密码后重新输入！";
                                          //  showToastView("密码错误！请核对密码后重新输入！");
                                            mHandler.sendMessage(msg);
                                            //handler.sendMessage(msg);
                                            alertDialog.dismiss();
                                        }
                                    }
                                }
                            });
                        }













/*
                        String pwd1 = systemValuesDao.dbQueryOneByName(ConstantValue.authorPassword).getValue();
                        String pwd2 = systemValuesDao.dbQueryOneByName(ConstantValue.manufactorPassword).getValue();

                        String strPwd = new String().format("%s", tv_InputPwd.getText());
                        if (strPwd.compareTo(pwd1) == 0 ||strPwd.compareTo(pwd2) == 0 ) {
                            //进入系统设置页面
                            openActivity(MainActivity_main_systemCorrelation.class);
                            AppManager.getInstance().killActivity(MainActivity_main.class);
                            //startActivity(intent);
                            alertDialog.dismiss();
                            mFloatballManager.closeMenu();

                        } else {
                            Message msg = Message.obtain();
                            msg.what = DOWN_ERROR;
                            msg.obj = "密码错误！请核对密码后重新输入！";
                            showToastView("密码错误！请核对密码后重新输入！");
                            //handler.sendMessage(msg);
                            alertDialog.dismiss();
                        }*/
                    }
                });
                //取消
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mFloatballManager.closeMenu();

                    }
                });
            }
        };


        MenuItem  manufactorItem = new MenuItem(BackGroudSeletor.getdrawble("ic_manufactory", this)) {
            @Override
            public void action() {
                Log.i(TAG, "action: 厂家设置");

                //系统设置按钮
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_main.this).create();
                alertDialog.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                final Window window = alertDialog.getWindow();


                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                hideBottomUIMenu();

                window.setContentView(R.layout.activity_login);
                final EditText tv_InputPwd = (EditText) window.findViewById(R.id.editInputPwd);
                final EditText tv_InputName = (EditText) window.findViewById(R.id.editInputName);
                Button btn_pwdOk = (Button) window.findViewById(R.id.btnPwdOK);
                Button btn_cancle = (Button) window.findViewById(R.id.btnCancle);
                //确认
                btn_pwdOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String pwd2 = systemValuesDao.dbQueryOneByName(ConstantValue.manufactorPassword).getValue();

                        String strPwd = new String().format("%s", tv_InputPwd.getText());
                        String strName = new String().format("%s", tv_InputName.getText());
                        if (strPwd.compareTo(pwd2) == 0 && strName.compareTo("njlsj")==0) {
                            //进入系统设置页面
                            openActivity(manufactorSetActivity.class);
                            AppManager.getInstance().killActivity(MainActivity_main.class);
                            //startActivity(intent);
                            alertDialog.dismiss();
                            mFloatballManager.closeMenu();
                        } else {
                            Message msg = Message.obtain();
                            msg.what = DOWN_ERROR;
                            msg.obj = "密码错误！请核对密码后重新输入！";
                            showToastView("密码错误！请核对密码后重新输入！");
                            // handler.sendMessage(msg);
                            alertDialog.dismiss();
                        }
                    }
                });
                //取消
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mFloatballManager.closeMenu();
                    }
                });
            }
        };

        MenuItem backItem = new MenuItem(BackGroudSeletor.getdrawble("ic_back", this)) {
            @Override
            public void action() {
                Log.i(TAG, "action: 返回");
                //AppManager.getInstance().killAllActivityNotMain();
                AppManager.getInstance().killActivity(MainActivity_main.class);
                mFloatballManager.closeMenu();
            }
        };
        mFloatballManager
                .addMenuItem(authorItem)
                .addMenuItem(backItem)
                .addMenuItem(manufactorItem)
                .buildMenu();
    }





    /**
     * 产品图片加载
     */
    public final int MSG_SET_IMAGE = 1;
    private TextView mtvNote;
    GridView gridView;
    private List<ImageInfo> imageInfoList;
    private GridAdapter myAdapter;
    private PopupWindow mPopupWindow;

    private View mButton2;
    //定时
    private BackMain backMain;
    //按钮设定
    private final int DOWN_ERROR = 1;

    //二维码
    private ImageView imageView2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);//加载进来

        //activity管理信息
        AppManager.getInstance().addActivity(this);


        init();
        boolean showMenu = true;//换成false试试
        initSinglePageFloatball(showMenu);
        //5 如果没有添加菜单，可以设置悬浮球点击事件
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    // toast("点击了悬浮球");
                }
            });
        }

////******************************************************************************
        gridView = (GridView)findViewById(R.id.tvgridview);

        imageView2 =(ImageView)findViewById(R.id.imageView2);
        //首页二维码图片


        imageInfoList.clear();
        List<String> imgtextList = new ArrayList<String >();
        imgtextList=productDao.dbQueryAllProductname();
   /*     for(int i = 0;i< imgtextList.size();i++){
            imgtextList.add(String.format("第%d个图",i+1));
        }*/
        List<String> imgList = new ArrayList<String>();
        imgList=productDao.dbQueryAllImgurl();
        // imgList.clear();

        initData(imgList,imgtextList);
        ////*****************************************************************************

        Equipment equipmenthost = equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment);

        File f = new File( xmppConnect.FILE_PATH +"/app.JEPG");
        //二维码信息
        if(f.exists()){
            imageView2.setImageBitmap( ConstantValue.bitmapTwoDimensionalCode);
            Log.i(TAG, "run: 设置二维码信息1");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getTwoDimensionalCode();
                    Message message = Message.obtain();
                    message.what = ConstantValue.GetBitmapTwoDimensionalCode;
                    message.obj = ConstantValue.bitmapTwoDimensionalCode;
                    mHandler.sendMessage(message);
                    // imageView2.setImageBitmap( ConstantValue.bitmapTwoDimensionalCode);
                    Log.i(TAG, "run: 设置二维码信息0");
                }

            }).start();
        }
      /*  if(null == ConstantValue.bitmapTwoDimensionalCode){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getTwoDimensionalCode();
                    Message message = Message.obtain();
                    message.what = ConstantValue.GetBitmapTwoDimensionalCode;
                    message.obj = ConstantValue.bitmapTwoDimensionalCode;
                    mHandler.sendMessage(message);
                    // imageView2.setImageBitmap( ConstantValue.bitmapTwoDimensionalCode);
                    Log.i(TAG, "run: 设置二维码信息0");

                }

            }).start();
        }else {
            imageView2.setImageBitmap( ConstantValue.bitmapTwoDimensionalCode);
            Log.i(TAG, "run: 设置二维码信息1");

        }*/

 /*       if ((null == ConstantValue.bitmapTwoDimensionalCode)) {
            // getTwoDimensionalCode();
        } else {
            Message message = Message.obtain();
            message.what = ConstantValue.GetBitmapTwoDimensionalCode;
            message.obj = ConstantValue.bitmapTwoDimensionalCode;
            mHandler.sendMessage(message);
        }*/
        //初始化系统界面的信息  货物出售信息


    }

    /////////////////产品图片加载************************************
    private void initData(final  List<String> imgL,final List<String> textL ) {
        new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < imgL.size(); i++) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setImagePath(imgL.get(i));
                    try {//getImageFromNetWork()//不再使用

                        imageInfo.setBitmap(  Glide.with(context)//使用glide加载图片信息。
                                .asBitmap()
                                .load(new mGlideUrl(imgL.get(i)))//设置缓存防止失效
                                 //必须
                                .into(500, 500)
                                .get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Glide.with(context).load(imgL.get(i));


                    //imageInfo.setBitmap(MyBitmapUtils.display(imgL.get(i)));
                    //  Glide.with(MainActivity.context).load(imgL.get(i)).into(imageView);

                    imageInfo.setText(textL.get(i));
                    Message msg = Message.obtain();
                    msg.what = MSG_SET_IMAGE;
                    msg.obj = imageInfo;
                    msg.arg1 = i+1;
                    mHandler.sendMessage(msg);

                    Log.i(TAG, "run: "+imgL.get(i)+"");
                }

            }
        }.start();

    }


    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SET_IMAGE:
                    ImageInfo imageInfo = (ImageInfo)msg.obj;
                    imageInfoList.add(imageInfo);
                    myAdapter.notifyDataSetChanged();
                    // Toast.makeText(context,imageInfo.getImagePath(),Toast.LENGTH_SHORT).show();
                    break;
              /*  case ConstantValue.GetBitmapTwoDimensionalCode:

                    ConstantValue.bitmapTwoDimensionalCode = (Bitmap)msg.obj;
                    imageView2.setImageBitmap(ConstantValue.bitmapTwoDimensionalCode);
                    Log.i(TAG, "handleMessage: 图片二维码");
                    break;*/

                case ConstantValue.GetBitmapTwoDimensionalCode99:

                    showToastView("密码错误！请核对密码后重新输入！");

                    break;

                default:
                    break;
            }
        }
    };

    public Bitmap getImageFromNetWork(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
/*
//异步操作信息  异步操作处理的信息进行okhttp的异步操作 下载图片
           okHttpClient= new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url("http://www.baidu.com")
                    .get().build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: 失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    System.out.println(response.body().string());
                    Message message = Message.obtain();
                    message.obj= response.body().string();
                    handler .sendMessage(message);


                }
            });*/

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    ///////////////////////////////

    private void init() {
        /////////////产品图片初始信息
        gridView = findViewById(R.id.tvgridview);
        imageInfoList = new ArrayList<ImageInfo>();
        myAdapter = new GridAdapter(context,imageInfoList);
        gridView.setAdapter(myAdapter);
        // TODO: 五分钟无人操作回主页面 定时器 无人操作自动跳转
        //backMain = new BackMain(1000 * 60 * 5, 1000, this);
        backMain = new BackMain(1000 * 30, 1000, MainActivity_main.this);

    }
    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        timeStart();
        super.onResume();

    }



    //region 无操作 返回主页

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
