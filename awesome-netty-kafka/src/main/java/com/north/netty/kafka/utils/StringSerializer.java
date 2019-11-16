package com.north.netty.kafka.utils;

import java.io.UnsupportedEncodingException;

public class StringSerializer {

    public static byte[] getBytes(String str){
        if(str == null){
            return null;
        }
        try {
            return str.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(byte [] bytes) {
        if(bytes == null){
            return null;
        }
        try {
            return new String(bytes, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
