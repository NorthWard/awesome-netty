package com.north.netty.redis.cmd.impl.getcmd;

import com.north.netty.redis.cmd.AbstractCmd;

import java.util.ArrayList;

/**
 *   get命令的抽象类
 *   命令参数
 *  get key
 * @author laihaohua
 */
public abstract class AbstractGetCmd<T> extends AbstractCmd<T>{

    public AbstractGetCmd(T  key){
        super();
        super.paramList = new ArrayList<>();
        paramList.add(key);
    }

    @Override
    protected String getCmd() {
        return super.cmd = "get";
    }
}
