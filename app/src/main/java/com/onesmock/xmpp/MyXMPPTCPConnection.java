package com.onesmock.xmpp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;




/**
 * Created by peter on 18/7/12.
 */
public class MyXMPPTCPConnection extends XMPPTCPConnection {

    private static final String TAG = "MyXMPPTCPConnection";
    static{
        try{
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static MyXMPPTCPConnection con;
    private MyXMPPTCPConnection(XMPPTCPConnectionConfiguration config) {
        super(config);
    }
    public static Handler mHandle = null;
    public static boolean IsOnline = false;

    public static synchronized MyXMPPTCPConnection getInstance(String ip) {
        //初始化XMPPTCPConnection相关配置
        if (true) {
            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            //设置连接超时的最大时间
            builder.setConnectTimeout(5000);
            //设置重连
            builder.setSendPresence(true);
            //设置登录openfire的用户名和密码
           builder.setUsernameAndPassword(ConstantValue.serverappXmppFriend, ConstantValue.serverappXmppFriend);
            //设置安全模式
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            builder.setResource("Android");
            //设置服务器名称  必须的参数
            builder.setServiceName(ConstantValue.serverappXmppServerName);

            // builder.setServiceName(ip);
            //设置主机地址
            builder.setHost(ip);
            //设置端口号
            builder.setPort(5222);
            //是否查看debug日志
            builder.setDebuggerEnabled(true);

            con = new MyXMPPTCPConnection(builder.build());

            con.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {
                    // TODO Auto-generated method stub

                    Log.i(TAG, "connected: 服务器连接成功");
                    if( null !=xmppConnect.handlerHost_to_Base){
                        IsOnline = true;


                        Message msg  = Message.obtain();
                        msg.obj = "服务器连接成功";
                        msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                        //mHandle.sendMessage(msg);
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);
                   }
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "authenticated: 服务器连接验证通过");
                    Message msg = Message.obtain();
                    msg.obj = "服务器连接验证通过";
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                    //mHandle.sendMessage(msg);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);
                }

                @Override
                public void connectionClosed() {
                    // TODO Auto-generated method stub
                    Message msg = Message.obtain();
                    msg.obj = "服务器连接关闭";
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                   // mHandle.sendMessage(msg);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);
                    Log.i(TAG, "connectionClosed: 服务器连接关闭");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    if(e.getMessage().contains("conflict")){

                        Log.i(TAG, "connectionClosedOnError: 抱歉,您被挤下线");
                        Message msg = Message.obtain();
                        msg.obj = "抱歉,您被挤下线";
                        msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                       // mHandle.sendMessage(msg);
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);
                    }
                }

                @Override
                public void reconnectionSuccessful() {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "reconnectionSuccessful:重连成功... ");


                    xmppConnect.initListenBoolean = false;//重新链接成功  设置新的监听
                    Message msg = Message.obtain();
                    msg.obj = "重连成功...";
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                   // mHandle.sendMessage(msg);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);

                }

                @Override
                public void reconnectingIn(int seconds) {
                    // TODO Auto-generated method stub
                    IsOnline = true;
                    Log.i(TAG, "reconnectingIn:重连中... ");
                    Message msg = Message.obtain();
                    msg.obj = "重连中...";
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                    //mHandle.sendMessage(msg);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);

                }

                @Override
                public void reconnectionFailed(Exception e) {
                    // TODO Auto-generated method stub
                    IsOnline = false;

                    Log.i(TAG, "reconnectionFailed: 重连失败");
                    Message msg = Message.obtain();
                    msg.obj = "重连失败...";
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                  //  mHandle.sendMessage(msg);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);


                }
            });


            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(con);
            reconnectionManager.setFixedDelay(15);//重联间隔
            reconnectionManager.enableAutomaticReconnection();//开启重联机制
            if(!con.isConnected()){
                try {
                    con.connect();

                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }
        return con;
    }


}

