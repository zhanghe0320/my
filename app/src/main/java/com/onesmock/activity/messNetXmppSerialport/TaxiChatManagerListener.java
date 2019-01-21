package com.onesmock.activity.messNetXmppSerialport;

import android.util.Log;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.tandong.sa.tag.helper.StringUtil;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 单人聊天信息监听类
 *
 * @author Administrator
 */
public class TaxiChatManagerListener implements ChatManagerListener {


    //监听只能启动一个

    private static final String TAG = "TaxiChatManagerListener";
    private static TaxiChatManagerListener taxiChatManagerListener = null;

    public void chatCreated(Chat chat, boolean arg1) {
        chat.addMessageListener(new ChatMessageListener() {
            public void processMessage(Chat arg0, Message msg) {
                String friend = msg.getFrom();
                friend = friend.substring(0, friend.indexOf("@"));
                //   Log.i(TAG, "processMessage: " + msg.getBody());
                Log.i(TAG, "processMessage: 添加监听");
                if (msg.getBody() != null/* && !xmppConnect.boversend*/) {
                    //  xmppConnect.boversend = true;//用来防止重复消息，出现多次监听的情况

                    android.os.Message msgg = android.os.Message.obtain();
                    msgg.what = ConstantValue.XMPP_MSG_CHAT_CALLBACK;
                    msgg.obj = msg.getBody();
                    xmppConnect.handlerHost_to_Base.sendMessage(msgg);

                    /**
                     *
                     * 在这里调用函数进行回复到PHP平台信息
                     */
                               /*   new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        xmppConnect.boversend = false;
                                        Log.i(TAG, "run: schedule oversend+++++++++++");
                                    }
                                }, 1000);*/
                }
            }
        });
    }


    public static TaxiChatManagerListener getInstance() {
        if (null == taxiChatManagerListener) {
            taxiChatManagerListener = new TaxiChatManagerListener();
        }
        return taxiChatManagerListener;
    }
}
