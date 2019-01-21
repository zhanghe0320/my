package com.onesmock.activity.BroadcastReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;


public class xmppChatManagerListenerService extends Service {
    private static final String TAG = "xmppChatManagerListener";
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        InitListen(xmppConnect.m_con);
        super.onCreate();
    }

    /**
     * onBind 是 Service 的虚方法，因此我们不得不实现它。
     * 返回 null，表示客服端不能建立到此服务的连接。
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new MyBinder();
    }

    // 已取代onStart方法--onStart方法是在Android2.0之前的平台使用的.
    // 在2.0及其之后，则需重写onStartCommand方法，同时，旧的onStart方法则不会再被直接调用
    // （外部调用onStartCommand，而onStartCommand里会再调用 onStart。在2.0之后，
    // 推荐覆盖onStartCommand方法，而为了向前兼容，在onStartCommand依然会调用onStart方法。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
    // IBinder是远程对象的基本接口，是为高性能而设计的轻量级远程调用机制的核心部分。但它不仅用于远程
    // 调用，也用于进程内调用。这个接口定义了与远程对象交互的协议。
    // 不要直接实现这个接口，而应该从Binder派生。
    // Binder类已实现了IBinder接口
    class MyBinder extends Binder {
        /**
         * 获取Service的方法
         * @return 返回PlayerService
         */
        public  xmppChatManagerListenerService getService(){
            Log.i(TAG, "getService: ");
            return xmppChatManagerListenerService.this;
        }
    }







    //xmpp下发消息的监听
        public  void InitListen(XMPPConnection m_con) {

            //设置定时ping
            if (m_con.isAuthenticated()) {
            }

            if(xmppConnect.initListenBoolean){

            }else {

                ChatManager chatManager = ChatManager.getInstanceFor(m_con);
                chatManager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        chat.addMessageListener(new ChatMessageListener() {
                            @Override
                            public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message msg) {
                                String friend = msg.getFrom();
                                friend = friend.substring(0, friend.indexOf("@"));
                                //   Log.i(TAG, "processMessage: " + msg.getBody());
                                if (msg.getBody() != null/* && !xmppConnect.boversend*/) {
                                    xmppConnect.boversend = true;

                                    Message msgg = Message.obtain();
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
                });

                //条件过滤器
                PacketTypeFilter packetFilter = new PacketTypeFilter(Presence.class);
                PacketListener packetListener = new PacketListener() {
                    @Override
                    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                        Presence presence = (Presence) packet;
                        String from = presence.getFrom();//发送方
                        String subfrom = from.substring(0, from.indexOf("@"));
                        String to = presence.getTo();//接收方
                        String subto = to.substring(0, to.indexOf("@"));
                        if (presence.getType().equals(Presence.Type.subscribe)) {
                            //发出好友申请
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "申请添加好友！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            xmppConnect.AgreeAdd(xmppConnect.m_con, subfrom);
                            Log.i(TAG, "processPacket: " + msg);
                            // Log.i("申请添加好友", "好友" + msg + "下线！");
                        } else if (presence.getType().equals(Presence.Type.subscribed)) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "恭喜，对方同意添加好友！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            Log.i(TAG, "processPacket: " + msg);
                            //Log.i("对方同意添加好友", "好友" + msg + "下线！");
                            //同意添加好友
                        } else if (presence.getType().equals(Presence.Type.unsubscribe)) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "抱歉，对方拒绝添加好友，或者删除了你！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            Log.i(TAG, "processPacket: " + msg);
                            //Log.i("抱歉，对方拒绝添加好友，或者删除了你！", "好友" + msg + "下线！");
                            // /拒绝添加好友or发出删除好友申请
                        } else if (presence.getType().equals(Presence.Type.unsubscribed)) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "unsubscribed";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            Log.i(TAG, "processPacket: " + msg);
                            // Log.i("好友" + msg + "下线！", "好友" + msg + "下线！");
                        } else if (presence.getType().equals(Presence.Type.unavailable)) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "好友" + subfrom + "下线！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            Log.i(TAG, "processPacket: " + msg);
                            // Log.i("好友" + subfrom + msg + "下线！", "好友" + subfrom + msg + "下线！");
                            //好友下线   要更新好友列表，可以在这收到包后，发广播到指定页面   更新列表
                        } else {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "好友" + subfrom + "上线！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            Log.i(TAG, "processPacket: " + msg);
                            // Log.i("好友" + subfrom + msg + "上线！", "好友" + subfrom + msg + "上线！");
                            //好友上线
                        }
                    }
                };
                Log.i(TAG, "InitListen: 监听开始！！！！");
                m_con.addPacketListener(packetListener, packetFilter);
                xmppConnect.initListenBoolean = true;

            }

        }


    

}
