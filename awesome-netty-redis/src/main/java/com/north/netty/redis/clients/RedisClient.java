package com.north.netty.redis.clients;

import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;

/**
 * @author laihaohua
 */
public interface RedisClient<T> {
    /**
     * set 命令 没有过期时间
     * @param key
     * @param v
     * @return
     */
    boolean set(T key, T v);
    /**
     * SETNX 命令
     * @param key
     * @param v
     * @return
     */
    boolean setNx(T key, T v);


    /**
     * 带有过期时间的set命令
     * @param key
     * @param v
     * @param seconds
     * @return
     */
    boolean setWithExpireTime(T key, T v, long seconds);

    /**
     * set key value [EX seconds] [PX milliseconds] [NX|XX]
     * @param key
     * @param v
     * @param expireMode
     * @param expireTime
     * @param x
     * @return
     */
    boolean set(T key, T v, ExpireMode expireMode, long expireTime, Xmode x);

    /**
     * get命令
     * @param key
     * @return
     */
    T get(T key);


}
