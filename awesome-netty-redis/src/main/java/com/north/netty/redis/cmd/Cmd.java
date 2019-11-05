package com.north.netty.redis.cmd;

/**
 * @author laihaohua
 */
public interface Cmd<PT> {
    /**
     * 构建RESP 协议
     * @return
     */
    PT build();
}
