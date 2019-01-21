package com.onesmock.activity.CountTimer;

import android.content.Context;
import android.os.CountDownTimer;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;


/**
 * 长时间不操作  返回主页进行广告展示
 */


public class BackMain extends CountDownTimer {

    private Context context;
   /* public static  BackMain backMain = null;


    public synchronized static BackMain getInstance(Context context, Activity activity) {
        if (backMain == null) {
            backMain = new BackMain(1000 * 10, 1000, activity);
        }
        return backMain;
    };*/

    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     */
    public BackMain(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }


    // 计时完毕时触发
    @Override
    public void onFinish() {

        //context.startActivity(new Intent(context, MainActivity.class));
        AppManager.getInstance().killAllActivityNotMain();
        BaseActivity.bundle.remove(ConstantValue.employeeId);//执行返回操作的时候，清除用户登陆信息..用户登陆信息

       // BaseActivity.SendMessage();//当设备自动退出到主页面，即广告页面的时候进行关门操作，防止操作人员忘记关门。关门信息处理，自动检测关门信息
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {

    }


}