package com.north.netty.redis.clients;

import com.north.netty.redis.cmd.impl.getcmd.binary.GetBinaryCmd;
import com.north.netty.redis.cmd.impl.getcmd.str.GetStringCmd;
import com.north.netty.redis.cmd.impl.setcmd.binary.SetBinaryCmd;
import com.north.netty.redis.enums.ClientType;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;
import com.north.netty.redis.exceptions.FailedToGetConnectionException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author laihaohua
 */
public class RedisBinaryClient extends AbstractRedisClient<byte []> implements RedisClient<byte []>{
    private static class InstanceHolder {
         static final RedisBinaryClient REDIS_CLIENT = new RedisBinaryClient();
    }

    protected RedisBinaryClient() {
        super(ClientType.BINARY);
    }
    public static RedisBinaryClient getInstance(){
        return InstanceHolder.REDIS_CLIENT;
    }


    @Override
    public boolean set(byte [] key, byte [] v){
        SetBinaryCmd setCmd = new SetBinaryCmd(key, v);
        try {
           return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean setNx(byte[] key, byte[] v) {
        SetBinaryCmd setCmd = new SetBinaryCmd(key, v, Xmode.NX);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setWithExpireTime(byte[] key, byte[] v, long seconds) {
        SetBinaryCmd setCmd = new SetBinaryCmd(key, v, ExpireMode.EX, seconds);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean set(byte[] key, byte[] v, ExpireMode expireMode, long expireTime, Xmode x) {
        SetBinaryCmd setCmd = new SetBinaryCmd(key, v, ExpireMode.EX, expireTime, x);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte[] get(byte [] key){
        GetBinaryCmd getBinaryCmd = new GetBinaryCmd(key);
        try {
            byte []  resp = invokeCmd(getBinaryCmd, getBinaryCmd);
            return resp;
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
