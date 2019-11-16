package com.north.netty.kafka.utils;

public class SimplePartitioner {
    public static int getPartion(String topic, byte [] key, byte[] val){
        // 简单地返回0
        return 0;
    }
}
