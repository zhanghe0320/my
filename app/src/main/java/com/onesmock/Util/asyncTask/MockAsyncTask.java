/*
 * Copyright (C) 2016 Tielei Zhang (zhangtielei.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onesmock.Util.asyncTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.onesmock.Util.serialPort.SerialPortActivity;
import com.onesmock.activity.base.BaseActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tielei Zhang on 16/9/1.
 *
 * 异步任务的一个假的实现.
 */
public class MockAsyncTask implements Task {
    public static long taskIdCounter;

    private String taskId;
    private TaskListener listener;

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Random rand1 = new Random();
    private Random rand2 = new Random();
    private static final String TAG = "MockAsyncTask";


    public  byte[] bytesMess = null;
    public MockAsyncTask(byte[] bytes) {
        taskId = String.valueOf(++taskIdCounter);
        bytesMess = bytes;

    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void start() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //任务随机执行0~3秒
             /*   try {
                    TimeUnit.MILLISECONDS.sleep(rand1.nextInt(3000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                Log.i(TAG, "run: 开始执行任务"+taskId);
                //模拟失败情况: 以80%的概率失败
                Exception error = null;
               /* if (rand2.nextInt(10) < 8) {
                    error = new RuntimeException("runtime error...");
                }*/
               if(null== bytesMess){

               }else {

                   BaseActivity.SendMessage(bytesMess);
                   Log.i(TAG, "-----"+taskIdCounter);
               }

                error = new RuntimeException("失败出错...");
                final Exception finalError = error;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            try {

                                    new Timer().schedule(new TimerTask() {//要延迟10秒进行指令的下发，硬件反馈时间是10秒
                                        @Override
                                        public void run() {
                                            if ( null !=BaseActivity.mstrComdata ) {
                                                //如果不为空 说明有数据返回 ，即指令执行完毕，进行下一个指令的执行。这是指令执行成功
                                                listener.taskComplete(MockAsyncTask.this);
                                                bytesMess= null;
                                            } else {
                                                listener.taskFailed(MockAsyncTask.this, finalError);//指令执行失败
                                                bytesMess= null;
                                            }
                                        }
                                    },  5500);

                            }
                            catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public void setListener(TaskListener listener) {
        this.listener = listener;
    }
}
