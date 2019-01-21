package com.onesmock.activity.main;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.onesmock.R;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.dialog.ConfirmDialog;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.administratorInformation.author.AuthorActivity;
import com.onesmock.activity.main.administratorInformation.messageTest.messageActivity;
import com.onesmock.activity.main.administratorInformation.netAddress.internetInformationActivity;
import com.onesmock.activity.main.administratorInformation.operationData.operationDataActivity;
import com.onesmock.activity.main.administratorInformation.operationManagement.operationManagementActivity;
import com.onesmock.activity.main.administratorInformation.shelvesManagement.shelvesManagementActivity;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.WifiActivity2;
import com.onesmock.activity.main.administratorInformation.wifi.wifiActivity;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 信息设置界面
 * 1.货架更新
 * 2.管理员信息
 * 3.添加设备
 * 4.货架出货信息
 * 5.wifi设置
 * 6.平台地址
 * 7.销售统计信息
 */
public class MainActivity_main_systemCorrelation extends BaseActivityO {

    //按钮设定

    private static final String TAG = "MainActivity_main_syste";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main_systemcorrelation);//加载进来
        //activity管理信息
        AppManager.getInstance().addActivity(this);
      //  NetMessFromPhp.getAllMessFromPhp(context);
        NetMessFromPhp.getJavaAll(context);//java
        backMain = new BackMain(1000 * 30, 1000, MainActivity_main_systemCorrelation.this);


    }


    //设备 管理信息 进行货架的产品的更新
    public void updateapp(View view) {
        Log.i(TAG, "onCreate: "+BaseActivity.getLocalVersionName()+"-----------------------"+BaseActivity.getLocalVersion());
      //  getPersimmions();
      //  checkUpdate();
        showDialogUpdate();
       // loadNewVersionProgress();
    }
    //检测本程序的版本，这里假设从服务器中获取到最新的版本号为3
    public void checkVersion(View view) {
        //如果检测本程序的版本号小于服务器的版本号，那么提示用户更新
        if (getVersionCode() < 3) {
            showDialogUpdate();//弹出提示版本更新的对话框


        }else{
            //否则吐司，说现在是最新的版本
            Toast.makeText(this,"当前已经是最新的版本",Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate() {

        ConfirmDialog confirmDialog = new ConfirmDialog(context, "发现新版本！请及时更新！", "确认", "取消");
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                // TODO Auto-generated method stub

                loadNewVersionProgress();//下载最新的版本程序
                confirmDialog.dismiss();

            }

            @Override
            public void doCancel() {
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
            }
        });


    }

    /**
     * 下载新版本程序
     */
    private void loadNewVersionProgress() {
        final   String uri=ConstantValue.https+xmppConnect.getPHPIpAddress()+ConstantValue.updateApp;
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
      //  pd.show();
        //启动子线程下载任务
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(uri, pd);
                    sleep(3000);
                   // BaseActivity.showToastView("下载完成！");
                    Log.i(TAG, "run: 下载完成！");
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                 //   Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "run: 下载新版本失败");
                   // BaseActivity.showToastView("下载新版本失败");
                    e.printStackTrace();
                }
            }}.start();
    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        //关键点：
        //安装完成后执行打开
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }


    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public static File getFileFromServer(String uri, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(uri);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time= System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(Environment.getExternalStorageDirectory()+"/Download", "app.apk");
            Log.i(TAG, "getFileFromServer: "+Environment.getExternalStorageDirectory()+"/Download");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }


    /*
     * 获取当前程序的版本名
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG", "版本号" + packInfo.versionCode);
        Log.e("TAG", "版本名" + packInfo.versionName);
        return packInfo.versionName;

    }


    /*
     * 获取当前程序的版本号
     */
    private int getVersionCode() {
        try {

            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            Log.e("TAG", "版本号" + packInfo.versionCode);
            Log.e("TAG", "版本名" + packInfo.versionName);
            return packInfo.versionCode;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return  1;
    }



    //设备 管理信息 进行货架的产品的更新
    public void operationManagementActivity(View view) {
        openActivity(operationManagementActivity.class);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }

    //出货等相关信息  数据的简单统计
    public void operationDataActivity(View view) {
        openActivity(operationDataActivity.class);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }

    //管理员信息查看  只能查看
    public void authorActivity(View view) {
        openActivity(AuthorActivity.class);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }


    //检查设备编号  添加更改设备
    public void shelvesManagementActivity(View view) {
        openActivity(shelvesManagementActivity.class);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }


    //wifi
    public void wifiActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("name", ConstantValue.wifi);
        openActivity(WifiActivity2.class,bundle);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }

    //网络设置
    public void internetInformationActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("name", ConstantValue.netAddress);
        openActivity(internetInformationActivity.class,bundle);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);

    }

    //测试
    public void testActivity(View view) {
        openActivity(messageActivity.class);
        AppManager.getInstance().killActivity(MainActivity_main_systemCorrelation.class);


    }



    //返回主页
    public void back_to_main(View view) {
        openActivity(MainActivity_main.class);
        AppManager.getInstance().killActivity(this);
        BaseActivity.bundle.remove(ConstantValue.employeeId);//执行返回操作的时候，清除用户登陆信息..用户登陆信息

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






/////////////////////////////////////////去除工具信息

   /* private void checkUpdate() {
        if(true){
            Version version = new Version();
            version.setUri("http://192.168.1.106:8080"+ConstantValue.updateApp);

            download(version);
        }
    }


    private void download(final Version version) {


        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("版本更新")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UpdateAppManager.downloadApk(MainActivity_main_systemCorrelation.this,version.getUri(),"版本升级","AppName");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

*/



    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
  /*  public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }*/

   /* @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            *//*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             *//*
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
*/
}
