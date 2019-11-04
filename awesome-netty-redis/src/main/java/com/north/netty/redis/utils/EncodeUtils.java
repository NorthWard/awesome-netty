package com.north.netty.redis.utils;

public class EncodeUtils {
    public static byte[] getBytes(Object object){
        return String.valueOf(object).getBytes();
    }
}
