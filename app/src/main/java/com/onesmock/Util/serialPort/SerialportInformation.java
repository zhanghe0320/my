package com.onesmock.Util.serialPort;

import android.util.Log;

public class SerialportInformation {

/**
 * 读取终端设备数据
 */
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();

            // 定义一个包的最大长度
            int maxLength = 2048;
            byte[] buffer = new byte[maxLength];
            // 每次收到实际长度
            int available = 0;
            // 当前已经收到包的总长度
            int currentLength = 0;
            // 协议头长度4个字节（开始符1，类型1，长度2）
            int headerLength = 4;

            while (!isInterrupted()) {
                try {
                 //   available = mInputStream.available();
                    if (available > 0) {
                        // 防止超出数组最大长度导致溢出
                        if (available > maxLength - currentLength) {
                            available = maxLength - currentLength;
                        }
                  //      mInputStream.read(buffer, currentLength, available);
                        currentLength += available;
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                int cursor = 0;
                // 如果当前收到包大于头的长度，则解析当前包
                while (currentLength >= headerLength) {
                    // 取到头部第一个字节
                    if (buffer[cursor] != 0x0F) {
                        --currentLength;
                        ++cursor;
                        continue;
                    }

                    int contentLenght = parseLen(buffer, cursor, headerLength);
                    // 如果内容包的长度大于最大内容长度或者小于等于0，则说明这个包有问题，丢弃
                    if (contentLenght <= 0 || contentLenght > maxLength - 5) {
                        currentLength = 0;
                        break;
                    }
                    // 如果当前获取到长度小于整个包的长度，则跳出循环等待继续接收数据
                    int factPackLen = contentLenght + 5;
                    if (currentLength < contentLenght + 5) {
                        break;
                    }

                    // 一个完整包即产生
                    // proceOnePacket(buffer,i,factPackLen);
                 //   onDataReceived(buffer, cursor, factPackLen);
                    currentLength -= factPackLen;
                    cursor += factPackLen;
                }
                // 残留字节移到缓冲区首
                if (currentLength > 0 && cursor > 0) {
                    System.arraycopy(buffer, cursor, buffer, 0, currentLength);
                }
            }
        }
    }

   /**
     * 获取协议内容长度
     */
    public int parseLen(byte buffer[], int index, int headerLength) {

//      if (buffer.length - index < headerLength) { return 0; }
        byte a = buffer[index + 2];
        byte b = buffer[index + 3];
        int rlt = 0;
        if (((a >> 7) & 0x1) == 0x1) {
            rlt = (((a & 0x7f) << 8) | b);
        }
        else {
            char[] tmp = new char[2];
            tmp[0] = (char) a;
            tmp[1] = (char) b;
            String s = new String(tmp, 0, 2);
            rlt = Integer.parseInt(s, 16);
        }

        return rlt;
    }

    private static final String TAG = "SerialportInformation";
    protected void onDataReceived(final byte[] buffer, final int index, final int packlen) {

        Log.i(TAG, "onDataReceived:收到信息 ");
        byte[] buf = new byte[packlen];
        System.arraycopy(buffer, index, buf, 0, packlen);
/*        ProtocolAnalyze.getInstance(myHandler).analyze(buf);
        ProtocolAnalyze.getInstance(myHandler).analyze(buf);*/
    }
}
