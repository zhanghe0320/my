package com.onesmock.activity.main.administratorInformation.netAddress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.FileUtils;
import com.onesmock.Util.dialog.ConfirmDialog;
import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.author.AuthorActivity;
import com.onesmock.activity.main.administratorInformation.operationManagement.operationManagementActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.activity.messNetXmppSerialport.NetMessFromPhp;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.R;
import com.onesmock.dao.SystemValues.SystemValuesDao;

import android_serialport_api.SerialPortFinder;

//设置网络平台的地址  本地设置 不需要上传
public class internetInformationActivity extends BaseActivityO {

    private static final String TAG = "internetInformationActi";
    private AlertDialog dialog;
    //按钮设定


    private LinearLayout update_system_baseinfo_xml;
    private Spinner mDevList;
    private SerialPortFinder mSerialPortFinder = new SerialPortFinder();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetinformation);
        update_system_baseinfo_xml = (LinearLayout) findViewById(R.id.update_system_baseinfo_xml);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, internetInformationActivity.this);


     //   mDevList = (Spinner) findViewById(R.id.spinnercomno);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //从intent对象中把封装好的数据取出来
        String name= null;
        if(null==bundle||("").equals(bundle)){
            name = ConstantValue.netAddress;
        }else{
            name = bundle.getString("name");
        }

        SystemValues pass = systemValuesDao.dbQueryOneByName(name);
        TextView update_pass_name = (TextView) findViewById(R.id.update_pass_name);//设置设备名称
        update_pass_name.setGravity(Gravity.CENTER);
        update_pass_name.setText(pass.getName());

        EditText update_pass_value = (EditText) findViewById(R.id.update_pass_value);//设置显示数据
        update_pass_value.setGravity(Gravity.CENTER);
        update_pass_value.setText(pass.getValue());
        update_pass_value.setSelection(update_pass_value.getText().length());

    }


    //设备 管理信息 进行货架的产品的更新
    public void system_baseinfo_update_save(View view) {



        ConfirmDialog confirmDialog = new ConfirmDialog(context, "确定修改吗?修改后不可恢复！?", "确认", "取消");
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                // TODO Auto-generated method stub

                EditText editText = (EditText) findViewById(R.id.update_pass_value);//找到数据
                Long time=  System.currentTimeMillis();


                SystemValuesDao  systemValuesDao =new SystemValuesDao(context);
                SystemValues  pass = systemValuesDao.dbQueryOneByPass(ConstantValue.netAddress);
                systemValuesDao.dbUpdateValue(editText.getText().toString(), time, pass.getId());
                confirmDialog.dismiss();
                //请求
               // NetMessFromPhp.getAllMessFromPhp(context);


                NetMessFromPhp.getJavaAll(context);//java



                FileUtils.DeleteFolder(xmppConnect.FILE_PATH);//删除二维码
                xmppConnect.xmppLogin(editText.getText().toString());//重新登陆

                xmppConnect.getPHPIpAddress();
                xmppConnect.getOpenfireIpAddress();
                AppManager.getInstance().killActivity(internetInformationActivity.class);
                openActivity(internetInformationActivity.class);
                xmppConnect.getPHPIpAddress();

            }

            @Override
            public void doCancel() {
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
            }
        });







       /* builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();

            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
*/

    }


    //设备 管理信息 返回上一个页面
    public void update_system_baseinfo(View view) {
        openActivity(MainActivity_main_systemCorrelation.class);
        AppManager.getInstance().killActivity(internetInformationActivity.class);

    }

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
