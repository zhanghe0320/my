package com.onesmock.dao.messfromPHP;

import android.util.Log;

import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.activity.main.administratorInformation.messageTest.messageActivity;
import com.onesmock.dao.equipment.EquipmentDao;

import java.util.Arrays;
import java.util.logging.Handler;


/**
 * 用于转换命令   String转byte
 * 使用中
 */
public class MessFromPHP {
    private static final String TAG = "MessFromPHP";
    private String equipment_host;
    private String equipment_base;
    private String message;
    private String mess;
    private String code;
    private String orderID;


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getEquipment_host() {
        return equipment_host;
    }

    public void setEquipment_host(String equipment_host) {
        this.equipment_host = equipment_host;
    }

    public String getEquipment_base() {
        return equipment_base;
    }

    public void setEquipment_base(String equipment_base) {
        this.equipment_base = equipment_base;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MessFromPHP{" +
                "equipment_host='" + equipment_host + '\'' +
                ", equipment_base='" + equipment_base + '\'' +
                ", message='" + message + '\'' +
                ", mess='" + mess + '\'' +
                ", code='" + code + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }

    //传入String
    public MessFromPHP(String s ){


    }


    //传入对象 返回String
    public String getStringMess(String s ){

        return "";
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {

        this.bytes = bytes;
    }

    //转换为 byte[]
    public static byte [] bytes = null;

    static StringBuffer stringBuffer= null;
    public static byte[] getByteMess(MessFromPHP s ){
        stringBuffer= new StringBuffer();
        stringBuffer.append(ConstantValue.messageHeaderSendToMachine);//55  消息头
        stringBuffer.append(s.getMessage());//01  消息//01  消息 指令信息
        stringBuffer.append(ConstantValue.messageLength);//  44    长度  发送的消息应当不进行16进制转换，发送的数据是十进制的数据



        //  String 转为byte   byte转为HEX    随后发送
        long longNum = Long.parseLong( s.getEquipment_base());
        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
        byte[] long2Bytes = byteUtil.long2Bytes(longNum);
        byteUtil.BinaryToHexString(long2Bytes);
        stringBuffer.append(byteUtil.BinaryToHexString(long2Bytes));//0000000000000001        01 0001 0000 0000 0001

        longNum = Long.parseLong( s.getEquipment_host());
        Log.i(TAG, "getByteMess: 测试的long值为：" + longNum);
        long2Bytes = byteUtil.long2Bytes(longNum);
        byteUtil.BinaryToHexString(long2Bytes);
        stringBuffer.append( byteUtil.BinaryToHexString(long2Bytes));//0000000000000001     2+2+2+16+16+2=40   02 0000 0000 0000 0001

        stringBuffer.append(ConstantValue.checkoutBit);//校验位   55 01 0b 00 00 00 00 00 00 00 01 0000000000000001 00
        if(stringBuffer.toString().length() ==40){
            bytes = byteUtil.hexStringToByteArray(stringBuffer.toString());

        }else {

        }

        String str0 = new String(bytes);
        System.out.println("----"+str0);
        return bytes;
    }



}