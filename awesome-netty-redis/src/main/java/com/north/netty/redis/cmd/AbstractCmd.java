package com.north.netty.redis.cmd;

import java.util.List;

/**
 * @author laihaohua
 */
public abstract class AbstractCmd<T> implements Cmd<T> {
    protected String cmd;
    protected List<T> paramList;
    /**
     *  redis命令
     * @return
     */
    protected abstract String getCmd();
}
