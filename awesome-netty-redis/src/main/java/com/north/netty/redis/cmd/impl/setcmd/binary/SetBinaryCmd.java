package com.north.netty.redis.cmd.impl.setcmd.binary;

import com.north.netty.redis.cmd.CmdResp;
import com.north.netty.redis.cmd.impl.setcmd.AbstractSetCmd;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;
import com.north.netty.redis.utils.CmdBuildUtils;
import com.north.netty.redis.utils.EncodeUtils;
import com.north.netty.redis.utils.SymbolUtils;

/**
 *  命令参数
 *  set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @author laihaohua
 */
public class SetBinaryCmd extends AbstractSetCmd<byte []>  implements CmdResp<byte [], Boolean> {
    /**
     * 没有过期时间
     * @param key
     * @param value
     */
    public SetBinaryCmd(byte [] key, byte []  value){
          this(key, value, null, 0, null);
    }
    public SetBinaryCmd(byte []  key, byte []  value, ExpireMode expireMode, long expireTime){
        this(key, value, expireMode, expireTime, null);

    }
    public SetBinaryCmd(byte []  key, byte []  value, Xmode xmode){
        this(key, value, null, 0, xmode);
    }
    public SetBinaryCmd(byte []  key, byte []  value, ExpireMode expireMode, long expireTime, Xmode xmode){
        super(  key,
                value,
                EncodeUtils.getBytes(expireMode),
                String.valueOf(expireTime).getBytes(),
                EncodeUtils.getBytes(xmode));
    }

    @Override
    public byte[] build() {
        return CmdBuildUtils.buildBinary(getCmd(), paramList);
    }

    @Override
    public Boolean parseResp(byte[] resp){
        if(resp == null){
            return false;
        }
        if(resp[0] == SymbolUtils.OK_PLUS_BYTE[0]){
            return true;
        }
        System.err.println("resp = [" + new String(resp) + "]");
        return false;
    }
}
