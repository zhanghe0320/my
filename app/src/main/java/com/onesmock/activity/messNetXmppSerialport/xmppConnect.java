package com.onesmock.activity.messNetXmppSerialport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.baidu.tts.auth.AuthInfo;
import com.google.gson.Gson;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.asyncTask.CallbackBasedTaskQueue;
import com.onesmock.Util.asyncTask.MockAsyncTask;
import com.onesmock.Util.asyncTask.Task;
import com.onesmock.Util.asyncTask.TaskQueue;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.main.MainActivity;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.messfromPHP.MessFromPHP;
import com.onesmock.xmpp.MyXMPPTCPConnection;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.chrono.MinguoEra;
import java.util.Timer;
import java.util.TimerTask;


public class xmppConnect  {
    public   String OpenfireIpAddress = null;
    public   String userName = null;
    public   String equipmentID = null;
    public   String PHPOpenfireIpAddress= null;
    //队列任务进行时
    public static  String getOpenfireIpAddress() {//获取OpenfireIpAddress  地址
             SystemValuesDao systemValuesDao = new SystemValuesDao(BaseActivity.context);
        Log.i(TAG, "getOpenfireIpAddress: "+systemValuesDao.dbQueryOneByName(ConstantValue.netAddress).getValue());
        return systemValuesDao.dbQueryOneByName(ConstantValue.netAddress).getValue();
    }


    public static  String getPHPIpAddress() {//获取php地址
            SystemValuesDao systemValuesDao = new SystemValuesDao(BaseActivity.context);

        return systemValuesDao.dbQueryOneByName(ConstantValue.netAddress).getValue();
    }


    public static  String getuserName() {//获取设备号
            SystemValuesDao systemValuesDao = new SystemValuesDao(BaseActivity.context);
        return systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getValue();
    }



    public static  String getEquipmentID() {
        Equipment EQ = null;
            EquipmentDao equipmentDao = new EquipmentDao(BaseActivity.context);
            EQ =  equipmentDao.dbQueryOneByEquipmentbase(xmppConnect.getuserName());
        return EQ.getEquipmentid();
    }


    public  static   TaskQueue taskQueue = null ;
    public    Task task = null;

    public static  TaskQueue getAsyncTask() {//获取队列
      /*  if(MockAsyncTask.taskIdCounter>=10){
            taskQueue= null;
        }*/
        if( taskQueue== null ){
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


        }
        return taskQueue;
    }



    private static final String TAG = "xmppConnect";
    public static boolean boversend = false;
    public  static MyXMPPTCPConnection m_con = null;
    public  static boolean binitOpenfire = false;




    //首页登陆
    public  static void loginXmpp(){
/*        if (!MyXMPPTCPConnection.IsOnline && !binitOpenfire) {
            binitOpenfire = true;*/
            taskQueue = xmppConnect.getAsyncTask();
            new Thread(new Runnable() {
                public void run() {
                    m_con = MyXMPPTCPConnection.getInstance(xmppConnect.getOpenfireIpAddress());
                    Log.i(TAG, "run: "+xmppConnect.getOpenfireIpAddress());
                    registerUser(m_con, xmppConnect.getuserName(), ConstantValue.ServiceAppPassWord);
                    m_con.isConnected();
                    try {
                        m_con.login(xmppConnect.getuserName(), ConstantValue.ServiceAppPassWord);
                        //Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                        Log.i("username",xmppConnect.getuserName());
                        Message msg = Message.obtain();
                        MyXMPPTCPConnection.IsOnline = true;
                        msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                        msg.obj = "登录成功";
                        Log.i(TAG, "run:登录成功 ");
                        addFriend(m_con, ConstantValue.serverappXmppFriend, ConstantValue.serverappXmppFriend);
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);//登陆成功 调用线程，接收数据

                        InitListen();

                /*        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message msg1 = Message.obtain();
                                msg.what = ConstantValue.XMPP_MSG_ADD_FRIEND;
                                msg.obj = "666";
                                mAddFriend.sendMessage(msg1);
                            }
                        },10*1000,30*1000);*/


                        //Chat chat = m_con.getChatManager().createChat(connectInfo, null);
                    } catch (XMPPException e) {
                        Message msg = Message.obtain();
                        msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                        msg.obj = "登录失败1";
                        Log.i(TAG, "run:登录失败1 ");
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);
                        e.printStackTrace();
                    } catch (SmackException e) {
                        Message msg = Message.obtain();
                        msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                        msg.obj = "登录失败2" + e.getMessage();
                        Log.i(TAG, "run:登录失败2 ");
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);
                        e.printStackTrace();
                    } catch (IOException e) {
                        Message msg = Message.obtain();
                        msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                        msg.obj = "登录失败3";
                        Log.i(TAG, "run:登录失败3 ");
                        xmppConnect.handlerHost_to_Base.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            }).start();
      /*  }*/
    }

    //添加好友
    public  static boolean addFriend(XMPPTCPConnection con, String friendName, String name) {
        Roster roster = Roster.getInstanceFor(con);
        //修改服务器主机地址的JID主机名部分
        try {
            roster.createEntry(friendName.trim() + "@" + con.getServiceName(), name, new String[]{"Friends"});
            MyXMPPTCPConnection.IsOnline = true;
            Message msg = Message.obtain();
            msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;
            msg.obj = "添加好友成功";
            Log.i(TAG, "addFriend: 添加好友成功");


            File f = new File( xmppConnect.FILE_PATH +"/app.JEPG");
            //二维码信息
            if(f.exists()){

                //获取本地二维码信息
                ConstantValue.bitmapTwoDimensionalCode  = xmppConnect.getBitmapFromLocal("app.JEPG");
                Message message = Message.obtain();
                message.what = ConstantValue.GetBitmapTwoDimensionalCode;
                message.obj = ConstantValue.bitmapTwoDimensionalCode;
                MainActivity.handlerImg.sendMessage(message);
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getTwoDimensionalCode();

                    }
                }).start();
            }
         /*   if(null == ConstantValue.bitmapTwoDimensionalCode){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getTwoDimensionalCode();
                    }
                }).start();
            }else {
                Message message = Message.obtain();
                message.what = ConstantValue.GetBitmapTwoDimensionalCode;
                message.obj = ConstantValue.bitmapTwoDimensionalCode;
                MainActivity.handlerImg.sendMessage(message);
            }*/

/*            BaseActivity.authInfo = BaseActivity.mSpeechSynthesizer.auth(ConstantValue.baiduTtsMode);
            if (!BaseActivity.authInfo.isSuccess()) {
                // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
                //  print("【error】鉴权失败 errorMsg=" + errorMsg);
                BaseActivity.authInfo = BaseActivity.mSpeechSynthesizer.auth(ConstantValue.baiduTtsMode);
                Log.i(TAG, "addFriend: 正在鉴权");

            }*/
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
        } catch (SmackException.NotLoggedInException e) {
            MyXMPPTCPConnection.IsOnline = false;
            Message msg = Message.obtain();
            msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;
            msg.obj = "添加好友失败,好友不存在";
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            MyXMPPTCPConnection.IsOnline = false;
            Log.i(TAG, "addFriend: 添加好友失败,好友不存在");
            e.printStackTrace();
            return false;
        } catch (SmackException.NoResponseException e) {
            MyXMPPTCPConnection.IsOnline = false;
            Message msg = Message.obtain();
            msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;
            msg.obj = "添加好友失败,对方没回复";
            Log.i(TAG, "addFriend: 添加好友失败,对方没回复");
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            MyXMPPTCPConnection.IsOnline = false;
            e.printStackTrace();
            return false;
        } catch (XMPPException.XMPPErrorException e) {
            MyXMPPTCPConnection.IsOnline = false;
            Message msg = Message.obtain();
            msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;
            msg.obj = "添加好友失败，协议不对";
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            e.printStackTrace();
            Log.i(TAG, "addFriend: 添加好友失败，协议不对"+ msg);
            MyXMPPTCPConnection.IsOnline = false;
            return false;
        } catch (SmackException.NotConnectedException e) {
            MyXMPPTCPConnection.IsOnline = false;
            Message msg = Message.obtain();
            msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;
            msg.obj = "添加好友失败，通信异常";
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            e.printStackTrace();
            Log.i(TAG, "addFriend: 添加好友失败，通信异常"+ msg);
            MyXMPPTCPConnection.IsOnline = false;
            return false;
        }
        return true;
    }


    //同意添加好友
    public static void AgreeAdd(XMPPTCPConnection con, String friendName) {
        Presence presenceRes = new Presence(Presence.Type.subscribed);
        presenceRes.setTo(friendName);
        try {
            con.sendPacket(presenceRes);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public static boolean initListenBoolean = false;

    //xmpp下发消息的监听
    public static void InitListen() {

        //设置定时ping
        if (m_con.isAuthenticated()) {
        }
/*        if(xmppConnect.initListenBoolean){

        }else {*/
        TaxiChatManagerListener chatManagerListener = new TaxiChatManagerListener();

            ChatManager chatManager = ChatManager.getInstanceFor(m_con);

            chatManager.removeChatListener(TaxiChatManagerListener.getInstance());
            chatManager.addChatListener(TaxiChatManagerListener.getInstance());//添加监听

           /* chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message msg) {
                            String friend = msg.getFrom();
                            friend = friend.substring(0, friend.indexOf("@"));
                            //   Log.i(TAG, "processMessage: " + msg.getBody());
                            if (msg.getBody() != null
                                // && !xmppConnect.boversend
                                    ) {
                              //  xmppConnect.boversend = true;//用来防止重复消息，出现多次监听的情况

                                Message msgg = Message.obtain();
                                msgg.what = ConstantValue.XMPP_MSG_CHAT_CALLBACK;
                                msgg.obj = msg.getBody();
                                xmppConnect.handlerHost_to_Base.sendMessage(msgg);

                               // 在这里调用函数进行回复到PHP平台信息

                              //  new Timer().schedule(new TimerTask() {
                                 //   @Override
                                 //   public void run() {
                                 //       xmppConnect.boversend = false;
                                 //       Log.i(TAG, "run: schedule oversend+++++++++++");
                                 //   }
                              //  }, 1000);
                            }
                        }
                    });
                }
            });*/

            //条件过滤器
            PacketTypeFilter packetFilter = new PacketTypeFilter(Presence.class);
            PacketListener packetListener = new PacketListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    Presence presence = (Presence) packet;
                    String from = presence.getFrom();//发送方
                    if (from.length() > 10 && from.contains("@")) {
                        String subfrom = from.substring(0, from.indexOf("@"));
                        String to = presence.getTo();//接收方
                        String subto = to.substring(0, to.indexOf("@"));
                        if (presence.getType().equals(Presence.Type.subscribe)) {
                            //发出好友申请
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                            msg.obj = "申请添加好友！";
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            AgreeAdd(m_con, subfrom);
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
                }
            };

            m_con.addPacketListener(packetListener, packetFilter);
            xmppConnect.initListenBoolean = true;

    //  }

    }




    //注册用户
    public static Boolean registerUser(XMPPTCPConnection connection, String username, String password) {
        Message msg = Message.obtain();
        msg.what = ConstantValue.XMPP_MSG_RETURN_INFO;

        if (connection == null) {
            try {
                connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            }
        }
        try {
            AccountManager.getInstance(connection).createAccount(xmppConnect.getuserName(), ConstantValue.ServiceAppPassWord);
            msg.obj = "注册账号成功";
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            Log.i(TAG, "registerUser: 注册账号成功"+ msg);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            msg.obj = "注册账号失败1" + e.getMessage();
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            Log.i(TAG, "registerUser: 注册账号失败1"+ msg);
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            msg.obj = "注册账号失败2" + e.getMessage();
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            Log.i(TAG, "registerUser: 注册账号失败2");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            msg.obj = "注册账号失败3" + e.getMessage();
            xmppConnect.handlerHost_to_Base.sendMessage(msg);
            Log.i(TAG, "registerUser: 注册账号失败3"+ msg);

        }
        return true;
    }



    //回复的消息
    public void SendMyChat(XMPPTCPConnection connection, String from, String friend, String content) {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Chat chat = chatManager.createChat(friend + "@" + connection.getServiceName());
        try {
            /**
             * 发送一条消息给xmpp平台
             */
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody("消息：" + content);
            message.setTo(friend + "@" + connection.getServiceName());
            message.setFrom(from + "@" + connection.getServiceName());
            //message.addExtension(received);
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除当前用户
     * @param
     * @return
     */
    /*public static boolean deleteAccount(XMPPConnection connection)
    {
        try {
            connection.getAccountManager().deleteAccount();
            return true;
        } catch (Exception e) {
            return false;
        }
    }*/

    public /*boolean*/ static void disconnectAccount()
    {
        try {
            m_con = MyXMPPTCPConnection.getInstance(xmppConnect.getOpenfireIpAddress());
            //这里需要先将登陆状态改变为“离线”，再断开连接，不然在后台还是上线的状态
            Presence presence = new Presence(Presence.Type.unavailable);
            Log.i(TAG, "disconnectAccount: 注销登陆");
            m_con.sendPacket(presence);
            m_con.disconnect();
            //return true;
        } catch (Exception e) {
            e.printStackTrace();
           // return false;
        }
    }
/*
    public static Handler mHandleXmpp = new Handler() {//登陆的时候调用的线程
        public void handleMessage(Message msg) {
            // String sa= SendMessageToPhp.SendMessToPhp(context,"56010b01010000000102020000000100");
            //System.out.println(sa+"   99999999");
            switch (msg.what) {
                case ConstantValue.XMPP_MSG_CONNECT_INFO://链接登陆信息

                    Log.i(TAG, "handleMessage: 收到的回复1"+ msg.obj);
                    Message msg0 = Message.obtain();
                    msg0.obj = msg.obj;
                    xmppConnect.handlerHost_to_Base.sendMessage(msg0);
                    // handlerHost_to_Base.sendMessage(msg0);//接收参数的时候调用的线程  判断数据 进行数据的拼接 发送给货架
                    break;
                case ConstantValue.XMPP_MSG_CHAT_CALLBACK:

                    Log.i(TAG, "handleMessage: 收到的回复2"+ msg.obj);
                    msg0 = Message.obtain();
                    msg0.obj = msg.obj;
                    xmppConnect.handlerHost_to_Base.sendMessage(msg0);
                    // mnoteInfo.setText(strNote);
                    break;
                case ConstantValue.XMPP_MSG_PACKET_CALLBACK:

                    Log.i(TAG, "handleMessage: 收到的回复3"+ msg.obj);
                    msg0 = Message.obtain();
                    msg0.obj = msg.obj;
                    xmppConnect.handlerHost_to_Base.sendMessage(msg0);
                    //handlerHost_to_Base.sendMessage(msg0);
                    //mnoteInfo.setText(strNote);
                    break;
                case ConstantValue.XMPP_MSG_RETURN_INFO://添加好友信息
                    Log.i(TAG, "handleMessage: 收到的回复4"+ msg.obj);

                    msg0 = Message.obtain();
                    msg0.obj = msg.obj;
                    xmppConnect.handlerHost_to_Base.sendMessage(msg0);
                    // handlerHost_to_Base.sendMessage(msg0);
                    //mnoteInfo.setText(strNote);
                    break;
                default:
                    break;
            }
        }
    };*/



    ///////////////********************xmpp****************************
    //再次添加好友
    public static Handler mAddFriend = new Handler() {
        public void handleMessage(Message msg) {

            if (!MyXMPPTCPConnection.IsOnline) {
                Log.i(TAG, "onCreate: "+MyXMPPTCPConnection.IsOnline+xmppConnect.binitOpenfire);
                new Thread(new Runnable() {
                    public void run() {
                        Log.i(TAG, "run: "+xmppConnect.getuserName());

                        m_con = MyXMPPTCPConnection.getInstance(xmppConnect.getOpenfireIpAddress());
                        try {
                            m_con.login(xmppConnect.getuserName(), ConstantValue.ServiceAppPassWord);
                            final Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                            msg.obj = "登录成功";
                            Log.i(TAG, "run: 登录成功");

                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            MyXMPPTCPConnection.IsOnline = true;
                            addFriend(m_con, ConstantValue.serverappXmppFriend, ConstantValue.serverappXmppFriend);

                            InitListen();


                /*            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Message msg1 = Message.obtain();
                                    xmppConnect.mAddFriend.sendMessage(msg1);
                                }
                            },  10*1000, 30 * 1000);*/

                        } catch (XMPPException e) {
                            //Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                            msg.obj = "登录失败1";
                            Log.i(TAG, "run: 登录失败1");
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            MyXMPPTCPConnection.IsOnline = false;
                            e.printStackTrace();
                        } catch (SmackException e) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                            msg.obj = "登录失败" + e.getMessage();
                            Log.i(TAG, "run: 登录失败2");
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            MyXMPPTCPConnection.IsOnline = false;
                            e.printStackTrace();
                        } catch (IOException e) {
                            Message msg = Message.obtain();
                            msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                            msg.obj = "登录失败3";
                            Log.i(TAG, "run: 登录失败3");
                            xmppConnect.handlerHost_to_Base.sendMessage(msg);
                            MyXMPPTCPConnection.IsOnline = false;
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else {

                addFriend(m_con, ConstantValue.serverappXmppServerName, ConstantValue.serverappXmppServerName);
            }
        }
    };


    public static int size = 0;
    //下发消息到货架
    public static Handler handlerHost_to_Base = new Handler() {//接收到消息之后进行消息的下发，下发给具体货架
        @Override
        public void handleMessage(Message msg) {
            //发送消息  不需要witch
            String Note = msg.obj.toString();

            if(Note.toLowerCase().contains((ConstantValue.OK).toLowerCase())){

            }

           // if(112==Note.length()){//确定上面传入的参数  进行指令下达
                int beginIndex = 0;
                    int endIndex = 0;
                    byte[] bytes = null;

                    if (Note.contains("{")) {
                        beginIndex = Note.indexOf("{");
                        endIndex = Note.lastIndexOf("}") + 1;
                    String a = Note.substring(beginIndex, endIndex);
                    // Log.i(TAG, "handleMessage: "+a);
                    //解析为对象数据
                    Gson gson = new Gson();
                    MessFromPHP messFrom = gson.fromJson(a, MessFromPHP.class);
                    Log.i(TAG, "handleMessage: "+messFrom.toString());
                    if(null ==messFrom.getOrderID()){//java平台数据

                        BaseActivity.orderID = "0";


                    }else{
                        BaseActivity.orderID = messFrom.getOrderID();//设置ID区分订单号，或者进行用户的区分
                        Log.i(TAG, "handleMessage: "+ messFrom.getOrderID());
                    }
                    Equipment equipment=BaseActivity.equipmentDao.dbQueryOneByEquipmentbase( messFrom.getEquipment_base());

                    // if(null!=equipment){
                    if(messFrom.getMessage().toLowerCase().contains((ConstantValue.deleteShelf).toLowerCase())){//删除货架信息
                        BaseActivity.equipmentDao.dbDeleteEquipment(messFrom.getEquipment_base(),messFrom.getEquipment_host());
                        BaseActivity.productDao.dbDeleteProduct(messFrom.getEquipment_host(),messFrom.getEquipment_base());

                    }else {


                        byte[] bytes0 = null;

                       // messFrom.setEquipment_host("020000000100000001");
                       // messFrom.setEquipment_base("010001000000000001");
                        bytes0 = MessFromPHP.getByteMess(messFrom);

                        // BaseActivity.SendMessage(bytes0);
                        if(null != bytes0){//上一个指令发送完毕之后  所有信息重置 为空
                            Task task = new MockAsyncTask(bytes0); //BaseActivity.SendMessage(bytes0);
                            //taskQueue = xmppConnect.getAsyncTask();
                            taskQueue.addTask(task);



                        }



                      /*    new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                arrayList.clear();
                            }
                        },  2000, 15 * 1000);
*/

//                        Log.i(TAG, "1+++++++"+ byteUtil.byte2HexStr(bytes0));
                    }

                }

                //}
                Note = "";

            //}

        }

    };


/*    public static Handler messHandlerSendMain = new Handler(){
        public void handleMessage(Message msg) {
            String  message = msg.obj.toString();
            byte[] bytes0= byteUtil.hexStringToByteArray(message);
            Log.i(TAG, "handleMessage: "+message+"+++++++++");
            BaseActivity.SendMessage(bytes0);
        }
    };*/

         /**
          * 注销
          * @return
          */
   /* public boolean logout() {
        if(!isConnected()) {
            return false;
        }
        try {
            connection.instantShutdown();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }
*/


    /**
     * 注销
     * @return
     */
    public static void logout() {
    /*    if(!isConnected()) {
            return false;
        }*/
    if(null != m_con){
        try {
            Log.i(TAG, "logout: xmpp登出");
            m_con.instantShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }



    //登陆，所有的xmpp openfire登陆信息
    public  static void xmppLogin(String username) {
        Log.i(TAG, "xmppLogin: 开始登陆信息");
        ConstantValue.bitmapTwoDimensionalCode = null;
        xmppConnect.initListenBoolean = false;
        logout();//先退出

        taskQueue = xmppConnect.getAsyncTask();
        try {
            m_con = MyXMPPTCPConnection.getInstance(xmppConnect.getOpenfireIpAddress());
            Presence presence = new Presence(Presence.Type.unavailable);
            m_con.sendPacket(presence);
            m_con.disconnect();
            m_con.instantShutdown();
        }catch (Exception e){

        }

        new Thread(new Runnable() {
            public void run() {
                m_con = MyXMPPTCPConnection.getInstance(xmppConnect.getOpenfireIpAddress());

                registerUser(m_con, username, ConstantValue.ServiceAppPassWord);
                Log.i(TAG, "run: 开始注册信息！");
                m_con.isConnected();
                try {
                    m_con.login(username, ConstantValue.ServiceAppPassWord);
                    //Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                    Log.i("username",xmppConnect.getuserName());
                    Message msg = Message.obtain();
                    MyXMPPTCPConnection.IsOnline = true;
                    msg.what = ConstantValue.XMPP_MSG_PACKET_CALLBACK;
                    msg.obj = "登录成功";
                    addFriend(m_con, ConstantValue.serverappXmppFriend, ConstantValue.serverappXmppFriend);
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);//登陆成功 调用线程，接收数据
                    InitListen();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    MainActivity.initTTs();//初始化百度语音 initTTs();


/*
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message msg1 = Message.obtain();
                            xmppConnect.mAddFriend.sendMessage(msg1);
                        }
                    },  10*1000, 30 * 1000);*/
                    //Chat chat = m_con.getChatManager().createChat(connectInfo, null);



                } catch (XMPPException e) {
                    //Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                    Message msg = Message.obtain();
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                    msg.obj = "登录失败1";
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);//登陆成功 调用线程，接收数据
                    e.printStackTrace();
                } catch (SmackException e) {
                    Message msg = Message.obtain();
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                    msg.obj = "登录失败2" + e.getMessage();
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);//登陆成功 调用线程，接收数据
                    e.printStackTrace();
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = ConstantValue.XMPP_MSG_CONNECT_INFO;
                    msg.obj = "登录失败3";
                    xmppConnect.handlerHost_to_Base.sendMessage(msg);//登陆成功 调用线程，接收数据
                    e.printStackTrace();
                }
                //
            }


        }).start();

    }


















    /**
     * 获取二维码
     */

    public static String post(String path,String params) throws Exception{
        HttpURLConnection httpConn=null;
        BufferedReader in=null;
        PrintWriter out=null;
        try {
            URL url=new URL(path);
            httpConn=(HttpURLConnection)url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            httpConn.setRequestProperty("Accept-Charset", "utf-8");
            httpConn.setRequestProperty("contentType", "utf-8");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            //发送post请求参数
            out=new PrintWriter(httpConn.getOutputStream());
            out.print(params);
            out.flush();

            //读取响应
            if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuffer content=new StringBuffer();
                String tempStr="";
                in=new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while((tempStr=in.readLine())!=null){
                    content.append(tempStr);
                }

                return content.toString();
            }else{
                throw new Exception("请求出现了问题!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            in.close();
            out.close();
            httpConn.disconnect();
        }
        return null;
    }




        //文件保存的路径
        //本地图片的路径位置
         public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";

        /**
         * 向本地SD卡写网络图片
         *
         * @param bitmap
         */
        public static void saveBitmapToLocal(String fileName, Bitmap bitmap) {
            try {//将图片保存到本地，只保存二维码信息
                // 创建文件流，指向该路径，文件名叫做fileName
                File file = new File(FILE_PATH, fileName);
                // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    // 文件夹不存在
                    fileParent.mkdirs();// 创建文件夹
                    Log.i(TAG, "saveBitmapToLocal: 创建本地文件夹");
                }
                // 将图片保存到本地
                Log.i(TAG, "saveBitmapToLocal: 本地保存文件！");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        new FileOutputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 从本地SD卡获取缓存的bitmap
         */
        public static Bitmap getBitmapFromLocal(String fileName) {
            try {//获取二维码信息
                File file = new File(FILE_PATH, fileName);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                            file));
                    Log.i(TAG, "getBitmapFromLocal: 获取本地的图片。");
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    public static Bitmap getImageFromNetWork(String path, String body) {
        try {
            Bitmap bitmap =null;
            File f = new File(FILE_PATH +"/app.JEPG");
            if(f.exists()){

                Log.i(TAG, "getImageFromNetWork: 获取本地图片");
                bitmap = getBitmapFromLocal("app.JEPG");

            }else {
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.write(body);
                out.flush();


                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);


               saveBitmapToLocal("app.JEPG", bitmap);//本地保存


               inputStream.close();

            }
            return bitmap;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
