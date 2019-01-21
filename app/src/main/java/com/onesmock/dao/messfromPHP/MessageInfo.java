package com.onesmock.dao.messfromPHP;

public class MessageInfo {
    public static class messageInfoIn{
        public messageInfoIn(Object obj){

        }
        private String v1;
        private byte [] mbytes  = null;
        public byte[] getSendData(){
            return  mbytes;
        }
    }
    public static class messageInfoOut{
        public messageInfoOut(Object obj){

        }
        private String v1;
        private byte [] mbytes  = null;
        public byte[] getSendData(){
            return  mbytes;
        }
    }
}
