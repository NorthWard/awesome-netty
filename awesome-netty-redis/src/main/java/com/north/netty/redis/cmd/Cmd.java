package com.north.netty.redis.cmd;

/**
 * @author laihaohua
 */
public interface Cmd<T> {
    /**
     * 构建RESP 协议
     * @return
     */
     T build();


}
