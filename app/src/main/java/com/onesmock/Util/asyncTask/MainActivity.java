/*
package com.onesmock.Util.asyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView description;
    private TextView logTextView;

    private TaskQueue taskQueue;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        description = (TextView) findViewById(R.id.description);
        logTextView = (TextView) findViewById(R.id.log_display);

        description.setText("本页面演示使用\"异步+Callback\"方式实现任务队");

        taskQueue = new CallbackBasedTaskQueue();
        taskQueue.setListener(new TaskQueue.TaskQueueListener() {
            @Override
            public void taskComplete(Task task) {
               // TextLogUtil.println(logTextView, "Task (" + task.getTaskId() + ") complete");
                Log.i(TAG, "taskComplete:任务完成 "+task.getTaskId());
            }

            @Override
            public void taskFailed(Task task, Throwable cause) {
               // TextLogUtil.println(logTextView, "Task (" + task.getTaskId() + ") failed, error message: " + cause.getMessage());
                Log.i(TAG, "taskFailed: 任务失败"+task.getTaskId());
            }
        });

        */
/**
         * 启动5个任务
         *//*

        for (int i = 0; i < 5; i++) {
            Task task = new MockAsyncTask();
            Log.i(TAG, "onCreate: 启动任务"+i);
            //TextLogUtil.println(logTextView, "Add task (" + task.getTaskId() + ") to queue");
            taskQueue.addTask(task);
        }

    }

    @Override
    protected void onDestroy() {
        taskQueue.setListener(null);
        taskQueue.destroy();

        super.onDestroy();
    }
}
*/
