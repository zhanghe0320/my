package com.onesmock.activity.main.administratorInformation.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.onesmock.activity.base.BaseActivity;

/**
 * Created by peter on 18/8/23.
 */

public class WLANListener {
    private static Context mContext;
    public static WLANBroadcastReceiver receiver = null;
    public static WLANStateListener mWLANStateListener = null;

    private static final String TAG = "WLANListener";
    public WLANListener(Context context) {
        mContext = BaseActivity.context;
        receiver = new WLANBroadcastReceiver();
    }

    public void register(WLANStateListener listener) {
        mWLANStateListener = listener;

        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public static void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    private class WLANBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                /** wifi状态改变 */
                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                    if (mWLANStateListener != null) {
                        Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_CHANGED_ACTION,检测状态." );
                        mWLANStateListener.onStateChanged();
                    }
                }
                /**
                 * WIFI_STATE_DISABLED    WLAN已经关闭
                 * WIFI_STATE_DISABLING   WLAN正在关闭
                 * WIFI_STATE_ENABLED     WLAN已经打开
                 * WIFI_STATE_ENABLING    WLAN正在打开
                 * WIFI_STATE_UNKNOWN     未知
                 */
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (mWLANStateListener != null) {
                            Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_DISABLED,已经关闭." );
                            mWLANStateListener.onStateDisabled();
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        if (mWLANStateListener != null) {
                            Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_DISABLING,正在关闭." );
                            mWLANStateListener.onStateDisabling();
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        if (mWLANStateListener != null) {
                            Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_ENABLED,已经打开." );
                            mWLANStateListener.onStateEnabled();
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        if (mWLANStateListener != null) {
                            Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_ENABLING,正在打开." );
                            mWLANStateListener.onStateEnabling();
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        if (mWLANStateListener != null) {
                            Log.e(TAG, "onReceive: WLANBroadcastReceiver --> onReceive--> WIFI_STATE_UNKNOWN, 未知." );
                            mWLANStateListener.onStateUnknow();
                        }
                        break;
                }
            }
        }






    }


    public interface WLANStateListener {
        void onStateChanged();

        void onStateDisabled();

        void onStateDisabling();

        void onStateEnabled();

        void onStateEnabling();

        void onStateUnknow();

    }
}
