package com.onesmock.dao.messfromPHP;

import android.content.Context;
import android.util.Log;

import com.onesmock.dao.product.Product;
import com.onesmock.dao.product.ProductDao;

public class MessFromBase {
    private static final String TAG = "MessFromBase";

    private String messageMachineSendToHeader;
    private String messageLength;
    private String messageFrombase;
    private String checkoutBit;
    private String equipment_host;
    private String equipment_base;
    private String messcode;

    private String productName;

    @Override
    public String toString() {
        return "MessFromBase{" +
                "messageMachineSendToHeader='" + messageMachineSendToHeader + '\'' +
                ", messageLength='" + messageLength + '\'' +
                ", messageFrombase='" + messageFrombase + '\'' +
                ", checkoutBit='" + checkoutBit + '\'' +
                ", equipment_host='" + equipment_host + '\'' +
                ", equipment_base='" + equipment_base + '\'' +
                ", messcode='" + messcode + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMesscode() {
        return messcode;
    }

    public void setMesscode(String messcode) {
        this.messcode = messcode;
    }

    public String getMessageMachineSendToHeader() {
        return messageMachineSendToHeader;
    }

    public void setMessageMachineSendToHeader(String messageMachineSendToHeader) {
        this.messageMachineSendToHeader = messageMachineSendToHeader;
    }

    public String getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(String messageLength) {
        this.messageLength = messageLength;
    }

    public String getMessageFrombase() {
        return messageFrombase;
    }

    public void setMessageFrombase(String messageFrombase) {
        this.messageFrombase = messageFrombase;
    }

    public String getCheckoutBit() {
        return checkoutBit;
    }

    public void setCheckoutBit(String checkoutBit) {
        this.checkoutBit = checkoutBit;
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

    //  private static


    public static MessFromBase getMessFromBase(String message,Context context){
        MessFromBase messFromBase= new MessFromBase();
        if(message.length()>=40){
            String header = message.substring(0, 2);//消息头

            String mess = message.substring(2, 4);//消息内容

            String leangth = message.substring(4, 6);//消息长度

            String equipment_host = message.substring(6, 22);//消息接收地址

            String equipment_base = message.substring(22, 38);//消息发送地址

            String checkout = message.substring(38, message.length());//消息发送地址

            ProductDao productDao = new ProductDao(context);

            Product product = productDao.dbQueryOneProduct(equipment_base);
            // Log.i(TAG, "getMessFromBase: "+product.toString());

            messFromBase.setCheckoutBit(checkout);
            messFromBase.setEquipment_host(equipment_host);
            messFromBase.setEquipment_base(equipment_base);
            messFromBase.setMessageLength(leangth);
            messFromBase.setMessageMachineSendToHeader(header);
            messFromBase.setMesscode(mess);
//            MessFromBase.setProductName(product.getProductName());




            Log.i(TAG, "getMessFromBase: "+messFromBase.toString());
        }


        /**
         *       String m1 = mstrComdata.substring(0, 2);//消息头

         String m2 = mstrComdata.substring(2, 4);//消息内容

         String m3 = mstrComdata.substring(4, 6);//消息长度

         String m4 = mstrComdata.substring(6, 24);//消息接收地址

         String m5 = mstrComdata.substring(24, 42);//消息发送地址

         String m6 = mstrComdata.substring(42, mstrComdata.length());//消息发送地址
         */





/*        EquipmentDao equipmentDao = new EquipmentDao(context);
        Equipment equipment= equipmentDao.dbQueryOneByEquipmentbase(equipment_base);*/

        return  messFromBase;
    }
}
