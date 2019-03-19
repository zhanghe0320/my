/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onesmock.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.danikula.videocache.HttpProxyCacheServer;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.banner.MyFileNameGenerator;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.Util.serialPort.SerialPortActivity;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Stack;


import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

import com.birbit.android.jobqueue.JobManager;

import com.birbit.android.jobqueue.log.CustomLogger;


import android.util.Log;

public class Application extends MultiDexApplication {

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    public static SerialPort mSerialPort = null;
    SystemValues valueSystem;
    SystemValuesDao valueSystemDao;
    private static final String TAG = "Application";


    public static Application application;


    public Stack<Activity> activityStack;
    //图片视频混播
    private HttpProxyCacheServer proxy;
    public static HttpProxyCacheServer getProxy(Context context) {
        Application app = (Application) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer proxy = new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
        return proxy;
    }


    public static Application getInstance() {
        application= new Application();
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
      //  LeakCanary.install(this);



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    /***************************** activity管理 start **********************************/

    /**
     * add Activity 添加Activity到栈
     */
    public synchronized void addActivity(Activity activity) {

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);

    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public synchronized Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public synchronized void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public synchronized void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public synchronized void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public synchronized void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();

    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    /***************************** activity管理 end **********************************/


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        valueSystemDao = new SystemValuesDao(this);
        if (mSerialPort == null) {
            /* Read serial port parameters */
          //  SharedPreferences sp = getSharedPreferences("android.serialport.sample_preferences", MODE_PRIVATE);
            String path = valueSystemDao.dbQueryOneByName(ConstantValue.serialPort).getValue();//"/dev/ttyS0";// sp.getString("DEVICE", "");

            int baud_rate = Integer.decode("9600");
            int data_bits = Integer.decode("8");
            int stop_bits = Integer.decode("1");
            int flow = 0;
            int parity = 'N';
            String flow_ctrl = "None";
            String parity_check = "None";
            /* Check parameters */
            if ((path.length() == 0) || (baud_rate == -1)) {
                throw new InvalidParameterException();
            }

            if (flow_ctrl.equals("RTS/CTS")) {
                flow = 1;
            } else if (flow_ctrl.equals("XON/XOFF")) {
                flow = 2;
            }

            if (parity_check.equals("Odd")) {
                parity = 'O';
            } else if (parity_check.equals("Even")) {
                parity = 'E';
            }

            /* Open the serial port */
            //mSerialPort = new SerialPort(new File("/dev/ttySAC2"), 9600, 8);
            mSerialPort = new SerialPort(new File(BaseActivity.strComNo), 9600, 8);
            //mSerialPort = new SerialPort(new File("/dev/ttyS0"), 9600, 8);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
	/*	if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}*/
    }


}
