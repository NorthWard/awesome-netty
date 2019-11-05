package com.north.netty.redis.cmd;


/**
 * @author laihaohua
 */
public interface CmdResp<PARAM,RETURN> {
    /**
     * 解析redis的resp
     * @param resp
     * @return
     */
    RETURN parseResp(PARAM resp);
}
