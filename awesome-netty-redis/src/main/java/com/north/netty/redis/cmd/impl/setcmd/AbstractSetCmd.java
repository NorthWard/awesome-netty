package com.north.netty.redis.cmd.impl.setcmd;

import com.north.netty.redis.cmd.AbstractCmd;
import com.north.netty.redis.cmd.CmdResp;

import java.util.ArrayList;

/**
 *   set命令的抽象类
 *   命令参数
 *  set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @author laihaohua
 */
public abstract class AbstractSetCmd<T> extends AbstractCmd<T> {

    public AbstractSetCmd(T  key, T  value, T expireMode, T expireTime, T xmode){
        super();
        super.paramList = new ArrayList<>();
        paramList.add(key);
        paramList.add(value);
        // 设置了过期时间
        if(expireMode != null){
            paramList.add(expireMode);
            paramList.add(expireTime);
        }
        // 设置了 XX或NX
        if(xmode != null){
            paramList.add(xmode);
        }
    }

    @Override
    protected String getCmd() {
        return super.cmd = "set";
    }
}
