package com.north.netty.redis.utils;

public class EncodeUtils {
    public static byte[] getBytes(Object object){
        if(object == null){
            return null;
        }
        return String.valueOf(object).getBytes();
    }
}
