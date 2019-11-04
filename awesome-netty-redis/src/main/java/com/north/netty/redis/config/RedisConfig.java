package com.north.netty.redis.config;

public class RedisConfig {
    public static String host = "localhost";
    public static int port = 6379;
    /**
     * 每个client创建的连接数
     */
    public static int connectionCount = 10;

    /**
     * 超时时间
     */
    public static int TIME_OUT_MS =  20;
}
