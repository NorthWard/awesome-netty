package com.north.netty.redis.cmd;

import java.util.List;

/**
 * @author laihaohua
 */
public abstract class AbstractCmd<T> implements Cmd<T> {
    /**
     * 具体是什么命令, 例如get set等待
     */
    protected String cmd;
    /**
     * 这个命令后面的参数
     */
    protected List<T> paramList;
    /**
     *  redis命令
     * @return
     */
    protected abstract String getCmd();

}
