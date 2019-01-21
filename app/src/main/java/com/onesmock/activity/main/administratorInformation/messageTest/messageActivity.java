package com.onesmock.activity.main.administratorInformation.messageTest;


import com.onesmock.activity.messNetXmppSerialport.xmppConnect;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.onesmock.activity.CountTimer.BackMain;
import com.onesmock.activity.base.AppManager;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.base.BaseActivityO;
import com.onesmock.activity.main.MainActivity_main_systemCorrelation;
import com.onesmock.activity.main.administratorInformation.author.AuthorActivity;
import com.onesmock.activity.main.administratorInformation.netAddress.internetInformationActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivity;
import com.onesmock.activity.main.manufactor.manufactorSetActivityUpdate;
import com.onesmock.dao.equipment.Equipment;
import com.onesmock.dao.equipment.EquipmentDao;
import com.onesmock.dao.product.ProductDao;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.Util.serialPort.ConstantValueSerialPort;
import com.onesmock.Util.serialPort.SerialPortActivity;
import com.onesmock.R;

/**
 * 测试使用的界面
 * TestMessage
 * 测试信息：主要测试出烟  开门等指令
 */
public class messageActivity extends BaseActivityO {

    private static final String TAG = "messageActivity";

    //数据
    private Context mContext;
    private Spinner mDevListEquipment;//集合设备名字
    private Spinner mDevListMessage;//集合命令

    private EquipmentDao equipmentDao;

    //回传主页处理信息

    //本地测试数据  不做数据上传
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //activity管理信息
        AppManager.getInstance().addActivity(this);
        backMain = new BackMain(1000 * 30, 1000, messageActivity.this);//返回主页
        mContext = this;
        equipmentDao =new EquipmentDao(this);
        productDao =new ProductDao(this);

        mDevListEquipment= findViewById(R.id.message_equipment_name);
        String[] itemsEquipment =   equipmentDao.dbQueryAllEquipmengName();
        String[] itemsEquipment1 = new String[itemsEquipment.length + 1];
        for (int i = 0;i< itemsEquipment.length;i++){
            itemsEquipment1[i+1] = itemsEquipment[i];
        }
        itemsEquipment[0] = "---请选择---";
       // ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsEquipment); //第二个参数表示spinner没有展开前的UI类型
        arrayAdapteritemSpinner adapteritemSpinner = new arrayAdapteritemSpinner(context,itemsEquipment);
        mDevListEquipment.setAdapter(adapteritemSpinner); //之前已经通过Spinner spin = (Spinner) findViewById(R.id.spinner);来获取spin对象
        int iposition = adapteritemSpinner.getPosition(itemsEquipment1[0] );
        if(-1 == iposition){
            mDevListEquipment.setSelection(0);
        }else {
            mDevListEquipment.setSelection(iposition);
        }
        // upd  mDevListMessage
        mDevListMessage= findViewById(R.id.message_equipment_mess);

        String[] itemsMess =new String []{"出烟","开门","关门"};
        String[] itemsMess1 = new String[itemsMess.length + 1];
        for (int i = 0;i< itemsMess.length;i++){
            itemsMess1[i+1] = itemsMess[i];
        }
        itemsMess1[0] = "---请选择---";
       // ArrayAdapter<String> bb = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsMess1); //第二个参数表示spinner没有展开前的UI类型
        arrayAdapteritemSpinner adapteritemSpinnerMess = new arrayAdapteritemSpinner(context,itemsMess1);
        mDevListMessage.setAdapter(adapteritemSpinnerMess); //之前已经通过Spinner spin = (Spinner) findViewById(R.id.spinner);来获取spin对象
        int iposition0 = adapteritemSpinnerMess.getPosition(itemsMess[0] );
        if(-1 == iposition){
            mDevListMessage.setSelection(0);
        }else {
            mDevListMessage.setSelection(iposition);
        }


    }




    //设备 管理信息 进行货架的产品的更新
    public void system_baseinfo_update_save(View view) {
        String equipmentName= "";
        String mess ="";
        equipmentName=    mDevListEquipment.getSelectedItem().toString();
        mess= mDevListMessage.getSelectedItem().toString();

        if ("---请选择---".equals(equipmentName)) {

            BaseActivity.showToastView("请正确选择设备。。。 ");
        }else{
            Equipment equipment=new Equipment();
            equipment=equipmentDao.dbQueryOneByEquipmentname(equipmentName);

            if(null == equipment || "".equals(equipment)){

                BaseActivity.showToastView("没有此设备，请检查。。。 ");
            }else {

                //获取数据进行传输
                // 05x6    不1234   5678    1234567890 12  1234567890 12

                /*        byte[] midbytes=isoString.getBytes("UTF8");
                //为UTF8编码
                byte[] isoret = srt2.getBytes("ISO-8859-1");
                //为ISO-8859-1编码*/
                StringBuffer mess0;
                String s = "";
                mess0 = new StringBuffer();
                mess0.append(ConstantValue.messageHeaderSendToMachine);//55  消息头

                byte[] bytes =null;
                if ("---请选择---".equals(mess)) {

                    BaseActivity.showToastView("指令错误！！！请选择指令！！ ");

                } else {
                    switch (mess) {
                        case "出烟":

                            mess0.append(ConstantValue.ShipmentsAuthor);//01  消息//01  消息
                            mess0.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据





                            //  String 转为byte   byte转为HEX    随后发送
                            long longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(equipmentName).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                            byte[] long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

                            longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                            long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001





                         // append();//01 01 00000000
                         //  append();//02 01 00000000
                            mess0.append(ConstantValue.checkoutBit);
                           // 55  0d 01010000001 02020000001

                       /*    Message  message = Message.obtain();
                           message.obj= mess0.toString();
                           xmppConnect.messHandlerSendMain.sendMessage(message);*/

                            byte[] bytes0= byteUtil.hexStringToByteArray(mess0.toString());
                            Log.i(TAG, "handleMessage: "+mess0.toString()+"+++++++++");
                            BaseActivity.orderID = BaseActivity.bundle.getString(ConstantValue.employeeId);//返回的是orderID
                            BaseActivity.SendMessage(bytes0);

                            break;
                        case "开门":

                            mess0.append(ConstantValue.openTheDoor);//01  消息
                            mess0.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据


                            //  String 转为byte   byte转为HEX    随后发送
                             longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(equipmentName).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                             long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

                            longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                            long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001



                            // mess0.append(equipmentDao.dbQueryOneByEquipmentname(equipmentName).getEquipmentbase());//01 01 00000000
                          //  mess0.append(equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmentbase());//02 01 00000000       mess0.append(ConstantValue.checkoutBit);
                            bytes0= byteUtil.hexStringToByteArray(mess0.toString());
                            Log.i(TAG, "handleMessage: "+mess0.toString()+"+++++++++");
                            BaseActivity.orderID = BaseActivity.bundle.getString(ConstantValue.employeeId);//返回的是orderID
                            BaseActivity.SendMessage(bytes0);
                         /*   message = Message.obtain();
                            message.obj= mess0.toString();
                            xmppConnect.messHandlerSendMain.sendMessage(message);*/

                            break;
                        case "关门":

                            mess0.append(ConstantValue.closeTheDoor);//01  消息
                            mess0.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据

                            longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(equipmentName).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                            long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

                            longNum = Long.parseLong( equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmentbase());
                            Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
                            long2Bytes = byteUtil.long2Bytes(longNum);
                            byteUtil.BinaryToHexString(long2Bytes);
                            mess0.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001


                            // mess0.append(equipmentDao.dbQueryOneByEquipmentname(equipmentName).getEquipmentbase());//01 01 00000000
                          //  mess0.append(equipmentDao.dbQueryOneByEquipmentname(ConstantValue.mainEquipment).getEquipmentbase());//02 01 00000000       mess0.append(ConstantValue.checkoutBit);

                            bytes0= byteUtil.hexStringToByteArray(mess0.toString());
                            Log.i(TAG, "handleMessage: "+mess0.toString()+"+++++++++");
                            BaseActivity.orderID = BaseActivity.bundle.getString(ConstantValue.employeeId);//返回的是orderID
                            BaseActivity.SendMessage(bytes0);
                          /*  message = Message.obtain();
                            message.obj= mess0.toString();
                            xmppConnect.messHandlerSendMain.sendMessage(message);*/


                            break;
                        default:
                            break;
                    }



                }
            }
        }

    }


    //设备 管理信息 返回上一个页面
    public void update_system_baseinfo(View view) {

        openActivity(MainActivity_main_systemCorrelation.class);
        AppManager.getInstance().killActivity(messageActivity.class);
    }



    @Override
    protected void onResume() {
        timeStart();
        super.onResume();


    }

    //region 无操作 返回主页

    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                backMain.start();

            }
        });
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                backMain.start();
                break;
            //否则其他动作计时取消
            default:
                backMain.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        backMain.cancel();
    }
}
