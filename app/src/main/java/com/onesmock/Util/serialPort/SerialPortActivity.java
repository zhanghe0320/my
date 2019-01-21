/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.onesmock.Util.serialPort;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;




import com.onesmock.R;
import com.onesmock.Util.ByteDate.byteUtil;
import com.onesmock.Util.DBUtil.ConstantValue;
import com.onesmock.activity.base.Application;
import com.onesmock.activity.base.BaseActivity;
import com.onesmock.dao.SystemValues.SystemValues;
import com.onesmock.dao.SystemValues.SystemValuesDao;
import com.onesmock.activity.messNetXmppSerialport.xmppConnect;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

import android_serialport_api.SerialPort;

public abstract class SerialPortActivity extends Activity {
	public  static String strComNo = null;
	public Application mApplication;
	public static SerialPort mSerialPort = null;
	public static OutputStream mOutputStream = null;
	public static InputStream mInputStream;
	private ReadThread mReadThread;
	private static final String TAG = "SerialPortActivity";
	//55 11 21  8 8
	public static final int mdatalength = 40;
	private static byte[] receivelist= new byte[256];//缓冲区大小 ljs
	private static int receiveLength =40;//接收的长度 ljs
	private static List<byte[]> ReceiveByteList = null;//ljs
	private static Date dateFlag=  new Date();//ljs
	private Context context;
	SystemValues valueSystem;
	SystemValuesDao valueSystemDao;



	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			Date dateFlag=  new Date();//ljs
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[256];
					if (mInputStream == null) {
						return;
					}

					//size = mInputStream.read(buffer);
					size = mInputStream.read(buffer,0,mdatalength);
					if(size > 0){
						onDataReceived(buffer,size);
						Log.i(TAG, "run: "+buffer.toString());
					}



				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void waitBagData(byte b[],int size,long diff)
	{
		byte finalCmdByte[] =new byte[256];
		int finalByteSize =0;
		if(receiveLength>0&&diff>100)
		{
			byte[] finalCmdByte1=new byte[receiveLength];
			for(int j=0;j<receiveLength;j++)
			{
				finalCmdByte1[j]=receivelist[j];
			}
			onDataReceived(finalCmdByte1, receiveLength);
			Log.i(TAG, "waitBagData: "+finalCmdByte1.toString());
			receiveLength=0;
		}
		if(receiveLength+size<256)
		{
			receiveLength=receiveLength+size;
		}
		else
			receiveLength=0;

		for(int i=receiveLength-size;i<receiveLength;i++)//拼包加入大缓冲区
		{
			receivelist[i]=b[i-receiveLength+size];
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SerialPortActivity.this.finish();
			}
		});
		b.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//strComNo = MyOperFun.ReadSharedPreferencesString(MyOperFun.KeyID.eComeNo,this);
		mApplication = (Application) getApplication();
		Log.i(TAG, "onCreate: ********************getSerialPort");
		new Thread(){
			@Override
			public void run() {
					if(strComNo==null){
						strComNo=new SystemValuesDao(context).dbQueryOneByName(ConstantValue.serialPort).getValue();
					}
				try {
					if(mSerialPort==null){
						mSerialPort = mApplication.getSerialPort();
					}
					if(mOutputStream==null){
						mOutputStream = mSerialPort.getOutputStream();
					}
					mInputStream = mSerialPort.getInputStream();
					/* Create a receiving thread */
					mReadThread = new ReadThread();
					mReadThread.start();
				} catch (SecurityException e) {
					DisplayError(R.string.error_security);
				} catch (IOException e) {
					DisplayError(R.string.error_unknown);
				} catch (InvalidParameterException e) {
					DisplayError(R.string.error_configuration);
				}
			}
		}.start();

	}

   //接收信息
	protected abstract void onDataReceived(final byte[] buffer, final int size);

	@Override
	protected void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}




	//发送信息
	public  static class  SendMessage extends Thread {
		public byte[] bytes;
		@Override
		public void run() {
			super.run();
			try {
				//for(int i = 0; i < 1;i++){

					mOutputStream.write(bytes);
					//Thread.sleep(5000);
				Log.i(TAG, "run: "+bytes);
				Log.i(TAG, "run: "+byteUtil.toHexString(bytes));
				Log.i(TAG, "run: "+byteUtil.byteArrayToHexString(bytes));


					Log.i(TAG, "run: "+byteUtil.byte2HexStr(bytes)+"-------------------");
			//}

			} catch (IOException e){
				e.printStackTrace();
			}/*catch (InterruptedException e ){
				e.printStackTrace();
			}*/

		}
	}



	public static synchronized void SendMessage(byte[] bytes){
		//防止丢包，发三次
	/*	if (null != BaseActivity.mstrComdata){
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Log.i(TAG, "SendMessage: "+byteUtil.byte2HexStr(bytes));
					SendMessage sendMessage = new SendMessage();
					sendMessage.bytes = bytes;
					sendMessage.start();
				}
			},5000);

		}*/


		Log.i(TAG, "SendMessage: "+byteUtil.byte2HexStr(bytes));
		SendMessage sendMessage = new SendMessage();
		sendMessage.bytes = bytes;
		sendMessage.start();

	}




}
