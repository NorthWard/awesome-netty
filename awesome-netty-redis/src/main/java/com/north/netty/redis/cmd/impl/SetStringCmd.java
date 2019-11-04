package com.north.netty.redis.cmd.impl;

import com.north.netty.redis.cmd.AbstractStringCmd;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;

import java.util.ArrayList;

/**
 *  命令参数
 *  set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @author laihaohua
 */
public class SetStringCmd extends AbstractStringCmd {
    private ExpireMode expireMode;
    private Xmode xmode;
    private long expireTime;
    private String key;
    /**
     * 没有过期时间
     * @param key
     * @param value
     */
    public SetStringCmd(String key, String value){
          this(key, value, null, 0, null);
    }
    public SetStringCmd(String key, String value, ExpireMode expireMode, long expireTime){
        this(key, value, expireMode, expireTime, null);
        if(expireMode == null){
            throw new NullPointerException();
        }
        if(expireTime < 0){
            throw  new IllegalArgumentException();
        }

    }
    public SetStringCmd(String key, String value, Xmode xmode){
        this(key, value, null, 0, xmode);
        if(xmode == null){
            throw new NullPointerException();
        }
    }
    public SetStringCmd(String key, String value, ExpireMode expireMode, long expireTime, Xmode xmode){
        super();
        this.key = key;
        this.expireMode = expireMode;
        this.xmode = xmode;
        this.expireTime = expireTime;
        super.paramList = new ArrayList<>();
        paramList.add(key);
        paramList.add(value);
        // 设置了过期时间
        if(expireMode != null){
            paramList.add(this.expireMode.getType());
            paramList.add(String.valueOf(this.expireTime));
        }
        // 设置了 XX或NX
        if(xmode != null){
            paramList.add(this.xmode.getType());
        }
    }
    @Override
    protected String getCmd() {
        return super.cmd = "set";
    }
}
