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


import android.util.Log;

import com.onesmock.activity.base.BaseActivity;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Tielei Zhang on 16/9/1.
 *
 * 基于Callback机制的队列实现
 */
public class CallbackBasedTaskQueue implements TaskQueue, Task.TaskListener {
    private static final String TAG = "TaskQueue";

    /**
     * Task排队的队列. 不需要thread-safe
     */
    private Queue<Task> taskQueue = new LinkedList<Task>();

    private TaskQueueListener listener;
    private boolean stopped;

    /**
     * 一个任务最多重试次数.
     * 重试次数超过MAX_RETRIES, 任务则最终失败.
     */
    private static final int MAX_RETRIES = 0;
    /**
     * 当前任务的执行次数记录(当尝试超过MAX_RETRIES时就最终失败)
     */
    private int runCount;

    @Override
    public synchronized void addTask(Task task) {
        //新任务加入队列
        //是否加锁   synchronized
        taskQueue.offer(task);
        task.setListener(this);

        if (taskQueue.size() == 1/* && !stopped*/) {
            //当前是第一个排队任务, 立即执行它
            launchNextTask();
        }
    }

    @Override
    public void setListener(TaskQueueListener listener) {
        this.listener = listener;
    }

    @Override
    public void destroy() {
        stopped = true;
    }

    private void launchNextTask() {
        //取当前队列头的任务, 但不出队列
        Task task = taskQueue.peek();
        if (task == null) {
            //impossible case
          //  Log.e(TAG, "impossible: NO task in queue, unexpected!");
            Log.e(TAG, "launchNextTask: 没有任务" );
            return;
        }

        Log.d(TAG, "start task (" + task.getTaskId() + ")");
        Log.d(TAG, "launchNextTask: 任务开启"+task.getTaskId());
        task.start();
        runCount = 1;
    }

    @Override
    public void taskComplete(Task task) {
        //Log.d(TAG, "task (" + task.getTaskId() + ") complete");
        Log.d(TAG, "taskComplete: 任务完成"+task.getTaskId());
       // if(null != BaseActivity.mstrComdata){
            finishTask(task, null);
      //  }

    }

    @Override
    public void taskFailed(Task task, Throwable error) {
    /*    if (runCount < MAX_RETRIES && !stopped) {
            //可以继续尝试
           // Log.d(TAG, "task (" + task.getTaskId() + ") failed, try again. runCount: " + runCount);
            Log.d(TAG, "taskFailed: 任务失败"+task.getTaskId());
            Log.i(TAG, "taskFailed: 重新执行");
            task.start();
            runCount++;
        } else {*/
            //最终失败
           // Log.d(TAG, "task (" + task.getTaskId() + ") failed, final failed! runCount: " + runCount);
            Log.d(TAG, "taskFailed: 任务失败2"+task.getTaskId());
            Log.i(TAG, "taskFailed: 最终失败");
            finishTask(task, error);
        //}
    }

    /**
     * 一个任务最终结束(成功或最终失败)后的处理
     * @param task
     * @param error
     */
    private synchronized void finishTask(Task task, Throwable error) {
        //是否加锁   synchronized
        //回调
        if (listener != null /*&& !stopped*/) {
            try {
                if (error == null) {
                    listener.taskComplete(task);
                }
                else {
                    listener.taskFailed(task, error);
                }
            }
            catch (Throwable e) {
                Log.e(TAG, "", e);
            }
        }
        task.setListener(null);

        //出队列
        taskQueue.poll();

        //启动队列下一个任务
        if (taskQueue.size() > 0 /*&& !stopped*/) {
            Log.i(TAG, "finishTask: 清除任务，开始下一个任务！");
            launchNextTask();
        }
    }

}
