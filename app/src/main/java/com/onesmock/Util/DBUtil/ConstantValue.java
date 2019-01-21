package com.onesmock.Util.DBUtil;


import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.tts.client.TtsMode;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.dao.equipment.Equipment;

/**
 * 指令以及一些基础信息常量
 */
public class ConstantValue {


   public static String njlsj_db = "njlsj_db";//二维码图片下载地址


   private static String  appName = null;
   public static Bitmap bitmapTwoDimensionalCode = null;//二维码图片下载地址

   //获取二维码的常量
   public static final int GetBitmapTwoDimensionalCode = 0;
   public static final int GetBitmapTwoDimensionalCode99 = 99;
   public static final int messHandleriIntMain1 = 1;
   public static final int messHandleriIntMain2 = 2;
   public static final int messHandleriIntMain3 = 3;
   public static final int messHandleriIntBase1 =4;
   public static final int messHandleriIntBase2 = 5;

   public static final int MSG_LOAD_IMAGE = 6;
   public static final int MSG_WIFI_MESS = 7;

   //xmpp相关d的常量
   public static final int XMPP_MSG_CONNECT_INFO = 1;
   public static final int XMPP_MSG_CHAT_CALLBACK = 2;
   public static final int XMPP_MSG_PACKET_CALLBACK = 3;
   public static final int XMPP_MSG_RETURN_INFO = 4;
   public static final int XMPP_MSG_ADD_FRIEND = 5;

   /*   public synchronized static ConstantValue getInstance(Context context) {
          if (appName == null) {
              appName = new DBOpenHelper(context,"njlsj.db", null, 1);
          }
          return appName;
      };*/
   private ConstantValue(Context context) {
      super();
      if(appName == null){
         SystemValuesDao systemValuesDao = new SystemValuesDao(context);
         appName = systemValuesDao.dbQueryOneByName(ConstantValue.localNumber).getValue();
      }
   }

   /**
    * 设备控制相关的参数
    * 与货架的控制链接
    */
   public static final String messageHeaderSendToMachine = "55";//主设备发出
   public static final String messageMachineSendToHeader = "56";//辅设备发出
   public static final String messageLength = "11";//字节长度

   public static final String Shipments = "01";//出货消息   成功之后回复的消息
   public static final String ShipmentsFail = "11";//出货失败的消息

   public static final String ShipmentsAuthor = "21";//工作人员使用的出货消息   成功之后回复的消息
   public static final String ShipmentsAuthorFail = "22";//工作人员出货消息   失败了

   public static final String openTheDoor = "02";//开门指令，成功
   public static final String openTheDoorFail = "12";//开门失败


   public static final String closeTheDoor = "03";//关门指令，成功
   public static final String closeTheDoorFail = "13";//关门指令  失败

   public static final String outOfStockYes = "04";//缺货
   public static final String outOfStockNo = "14";//缺货

   public static final String portObjects = "05";//出口有物体
   public static final String equipmentFailureOne = "06";//设备故障一
   public static final String equipmentFailureTwo = "07";//设备故障二

   public static final String illegalOPenDoor = "08";//非法开门


   public static final String checkoutBit = "00";//校验位




   public static final String ServiceApp = "ServiceApp";//向平台请求数据

   public static final String ServiceAppPassWord = "666666";//向平台请求数据


   public static final int messageHeaderSendToMachineint = 55;//主设备发出
   public static final int messageMachineSendToHeaderint = 56;//辅设备发出
   // public static final int messageLengthint =0D ;//字节长度


   public static final int Shipmentsint = 01;//出货消息   成功之后回复的消息
   public static final int ShipmentsFailint = 17;//出货失败的消息

   public static final int ShipmentsAuthorint = 33;//工作人员出货消息   成功之后回复的消息
   public static final int ShipmentsAuthorFailint = 34;//工作人员出货消息   失败了

   public static final int openTheDoorint = 02;//开门指令，成功
   public static final int openTheDoorFailint = 18;//开门失败


   public static final int closeTheDoorint = 03;//关门指令，成功
   public static final int closeTheDoorFailint = 19;//关门指令  失败


   public static final int outOfStockint = 04;//缺货
   public static final int portObjectsint = 05;//出口有物体
   public static final int equipmentFailureOneint = 06;//设备故障一
   public static final int equipmentFailureTwoint = 07;//设备故障二
   public static final int illegalOPenDoorint = 8;//非法开门   非法开门是8  不是08

   public static final int checkoutBitint = 00;//校验位
   //public static final String ServiceApp="ServiceApp";//向平台请求数据





   public static final int PHP_Back_Success = 0;//P出烟信息是否需要记录
   public static final int PHP_Back_Fail = 1;//P出烟信息是否需要记录

   public static final int Okhttp_Back_Success = 200;//P出烟信息是否需要记录


   /**
    * xmpp相关的参数
    */
   public static final String serverappXmppFriend = "serverapp";//需要添加的好友
   public static final String serverappXmppServerName = "serverapp";//服务器名称


   /**
    * 涉及PHP平台的相关参数信息  网络地址
    */
   public static final String phpOpenDoorUrl = "/app.php?c=InstructionNotify&a=acceptOpenDoor";//PHP平台进行开门的指令反馈地址
   public static final String phpStockoutUrl = "/app.php?c=InstructionNotify&a=acceptStockout";//PHP平台缺货的指令反馈地址
   public static final String phpForeignUrl = "/app.php?c=InstructionNotify&a=acceptForeign";//PHP平台出口有物体的指令反馈地址
   public static final String phpEquipmentTroubleUrl = "/app.php?c=InstructionNotify&a=acceptEquipmentTrouble";//PHP平台故障1的指令反馈地址
   public static final String phpEquipmentTrouble2Url = "/app.php?c=InstructionNotify&a=acceptEquipmentTrouble2";//PHP平台故障2的指令反馈地址
   public static final String phpCloseDoorUrl = "/app.php?c=InstructionNotify&a=acceptCloseDoor";//PHP关门指令反馈地址
   public static final String phpAcceptDispatchUrl = "/app.php?c=InstructionNotify&a=acceptDispatch";//PHP出烟指令反馈地址



   /**
    *主动请求的数据
    */
   public static final String phpChangeOrAddProduct = "/app.php?c=Shelf&a=updateGoods";//PHP平台请求更换添加货物
   public static final String phpAddShelfUrl = "/app.php?c=Shelf&a=addShelf";//PHP平台请求添加货架
   public static final String phpPrematchUrl = "/app.php?c=Equipment&a=getPreMsg";//PHP平台请求添加货架
   public static final String phpGetAuthorUrl = "/app.php?c=Equipment&a=getWorkerList";//PHP平台请求管理人员信息
   public static final String phpShelfListUrl = "/app.php?c=Equipment&a=getShelfList";//PHP请求货架信息
   public static final String phpGoodsListUrl = "/app.php?c=Equipment&a=getGoodsList";//PHP请求现有商品
   public static final String phpStatisticsUrl = "/app.php?c=Shelf&a=getStatistics";//获取日出货量
   public static final String phpAddGoodsUrl = "/app.php?c=Shelf&a=addGoods";//添加货架的产品
   public static final String phpgetStatisticsUrl = "/app.php?c=Equipment&a=getStatistics";//获取所有货架的日出货量
   public static final String phpgetEquipmentMsgUrl = "/app.php?c=Equipment&a=getEquipmentMsg";//获取所有货架的日出货量
   public static final String phpgetTwoDimensionalCode = "/Public/Upload/Qrcode/";//获取二维码地址
   public static final String phpgetAccessToken  ="/app.php?c=Wx&a=getAccessToken";//获取AccessToken
   public static final String phpImage = ".png";//获取二维码地址
   public static final String openfireAddress = "www.nj-lsj.com";//PHP平台测试地址
   public static final String phpgetImgList = "www.nj-lsj.com";//获取产品相册的数据
   public static final String phpgetGoodsImgUrl = "/app.php?c=Equipment&a=getGoodsImg";//获取正在出售产品的图片



   /**
    *php平台的一些变量
    */
   public static final String GetBitmapTwoDimensionalCodeAddress = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";//获取二维码地址
   public static final String https = "https://";//PHP平台进行开门的请求回复  成功
   public static final String http = "http://";//PHP平台进行开门的请求回复  成功


   public static final String phpIs_successYes = "Y";//PHP平台进行开门的请求回复  成功
   public static final String phpIs_successNo = "N";//PHP平台进行开门的请求回复   失败
   public static final String phpOpenDoorUrl_Test = "http://njlsj.mynatapp.cc";//PHP平台测试地址
   public static final String phpAndroid = "android";//PHP平台发送请求必须的参数
   public static final String phpVersion = "1.0";//PHP平台发送请求必须的参数

   public static final String equipment_host = "equipment_host";//PHP平台   主机
   public static final String equipment_base = "equipment_base";//PHP平台   辅机
   public static final String is_success = "is_success";//PHP平台  是否成功
   public static final String platform = "platform";//PHP平台  android平台
   public static final String fail_reason = "fail_reason";//PHP平台  android平台
   public static final String remark = "remark";//PHP平台   备注
   public static final String illegal = "illegal";//非法 java   备注



   public static final String orderID = "orderID";//  是否成功   java平台的参数信息
   public static final String updateApp = "/data/app.apk";//  是否成功   java平台的参数信息  更新app地址，下载app地址
   public static final String employeeId = "employeeId";//  是否成功   java平台的参数信息 employeeId
   public static final String passWord="passWord";


   public static final String code = "code";//PHP平台返回的信息
   public static final String msg = "msg";//PHP平台返回的信息
   public static final String date = "date";//PHP平台返回的信息
   public static final String list = "list";//PHP平台返回的信息
   public static final String mess = "mess";//PHP平台返回的信息
   public static final String message = "message";//PHP平台返回的信息
   public static final String OK = "ok";//PHP平台下发的消息
   public static final String deleteShelf = "deleteShelf";//PHP平台下发的消息 删除货架信息





   public static final String codeValue0 = "0";//PHP平台返回的信息
   public static final String codeValue1 = "1";//PHP平台返回的信息
   public static final String msgValue = "成功";//PHP平台返回的信息
   public static final int codeValueInt0 = 0;//PHP平台返回的信息
   public static final int codeValueInt1 = 1;//PHP平台返回的信息

   public static final String localNumber = "本机编号";//PHP平台返回的信息
   public static final String authorPassword = "初始密码";//PHP平台返回的信息
   public static final String manufactorPassword = "超级密码";//PHP平台返回的信息
   public static final String serialPort = "串口号码";//PHP平台返回的信息
   public static final String netAddress = "网络地址";//PHP平台返回的信息
   public static final String wifi = "wifi";//PHP平台返回的信息
   public static final String mainEquipment = "主设备";//PHP平台返回的信息


   public static final String javaUrl = "192.168.1.113:8080";//java  平台地址
   public static final String javaRecordMsg = "/OSS/recordMsg.do";//java平台返回的信息
   public static final String javaMechineShelfList = "/OSS/mechineShelfList.do";//获取货架信息设备号、货架号、现有产品、现有产品照片、预配产品、预配产品照片）
   public static final String javaRecordMsgOpen = "/OSS/recordMsgOpen.do";//java  反馈开门信息
   public static final String javaRecordMsgClose = "/OSS/recordMsgClose.do";//java 反馈关门信息
   public static final String javaReadyProductList = "/OSS/readyProductList.do";//java 反馈预配信息，修改配货信息
   public static final String javaLackOfProduct = "/OSS/lackOfProduct.do";//java  反馈缺货、假缺货、不缺货日志记录
   public static final String javaOffAndOnLine = "/OSS/OffAndOnLine.do";//java  反馈  离线在线
   public static final String javaUpdateOrderByOrderId = "/OSS/updateOrderByOrderId.do";//java 反馈订单状态
   public static final String javaMechineLogin = "/OSS/mechineLogin.do";//java 管理员登陆设备信息
   public static final String javaAddProductToMechine = "/OSS/addProductToMechine.do";//java 添加产品信息
   public static final String javaBindShelf = "/OSS/bindShelf.do";//java 添加产品信息
   public static final String javaGetAccessToken = "/OSS/getAccessToken.do";//java 添加产品信息
   public static final String javaGetVideo = "/OSS/getVideo.do";//java 获取视频信息











   //百度语音相关信息
   // ================== 初始化参数设置开始 ==========================
   /**
    * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
    * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
    */
   public static String baiduAppId = "14397403";

   public static String baiduAppKey = "hzB6L4Ydk3EEh2q2iPBeASqm";

   public static String baiduSecretKey = "AC5ovlLtYDw7dG7pyfmTVr2n1Mj9TcnU";


   // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
   public static TtsMode baiduTtsMode = TtsMode.MIX;

   // ================选择TtsMode.ONLINE  不需要设置以下参数; 选择TtsMode.MIX 需要设置下面2个离线资源文件的路径
   public static final String baiduTEMP_DIR = "/sdcard/baiduTTS"; // 重要！请手动将assets目录下的3个dat 文件复制到该目录

   // 请确保该PATH下有这个文件
   public static final String baiduTEXT_FILENAME = baiduTEMP_DIR + "/" + "bd_etts_text.dat";
// /sdcard/baiduTTS/baiduTTS/bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat
   // 请确保该PATH下有这个文件 ，m15是离线男声
   public static final String baiduMODEL_FILENAME =
           baiduTEMP_DIR + "/" +"bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";// "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
   // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================


}

