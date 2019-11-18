package com.north.netty.redis.cmd.impl.setcmd.str;

import com.north.netty.redis.cmd.CmdResp;
import com.north.netty.redis.cmd.impl.setcmd.AbstractSetCmd;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;
import com.north.netty.redis.utils.CmdBuildUtils;
import com.north.netty.redis.utils.SymbolUtils;

/**
 *  命令参数
 *  set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @author laihaohua
 */
public class SetStringCmd extends AbstractSetCmd<String>  implements CmdResp<String, Boolean> {
    /**
     * 没有过期时间
     * @param key
     * @param value
     */
    public SetStringCmd(String key, String value){
          this(key, value, null, 0, null);
    }

    /**
     *
     * @param key
     * @param value
     * @param expireMode
     * @param expireTime
     */
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


    /**
     * 构建请求参数RESP
     * @return
     */
    @Override
    public String build() {
        return CmdBuildUtils.buildString(getCmd(), paramList);
    }

    /**
     * 解析redis返回的RESP
     * @param resp
     * @return
     */
    @Override
    public Boolean parseResp(String resp) {
        char ch = resp.charAt(0);
        // 一般返回 +OK 就是成功
        if(ch == SymbolUtils.OK_PLUS.charAt(0)){
              return true;
        }
        // 其他的都是失败
        return false;
    }
}
