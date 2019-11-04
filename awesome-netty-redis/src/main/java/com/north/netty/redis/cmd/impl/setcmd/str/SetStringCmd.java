package com.north.netty.redis.cmd.impl.setcmd.str;

import com.north.netty.redis.utils.CmdBuildUtils;
import com.north.netty.redis.cmd.impl.setcmd.AbstractSetCmd;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;

/**
 *  命令参数
 *  set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @author laihaohua
 */
public class SetStringCmd extends AbstractSetCmd<String> {
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

    }
    public SetStringCmd(String key, String value, Xmode xmode){
        this(key, value, null, 0, xmode);
    }
    public SetStringCmd(String key, String value, ExpireMode expireMode, long expireTime, Xmode xmode){
        super( key,
               value ,
               expireMode == null ? null : expireMode.getType(),
               String.valueOf(expireTime),
               xmode == null ? null : xmode.getType() );
    }


    @Override
    public String build() {
        return CmdBuildUtils.buildString(getCmd(), paramList);
    }
}
