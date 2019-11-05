package com.north.netty.redis.clients;

import com.north.netty.redis.cmd.impl.getcmd.str.GetStringCmd;
import com.north.netty.redis.cmd.impl.setcmd.str.SetStringCmd;
import com.north.netty.redis.enums.ClientType;
import com.north.netty.redis.enums.ExpireMode;
import com.north.netty.redis.enums.Xmode;
import com.north.netty.redis.exceptions.FailedToGetConnectionException;

/**
 * @author laihaohua
 */
public class RedisStringClient extends AbstractRedisClient<String> implements RedisClient<String> {
    protected RedisStringClient() {
        super(ClientType.STRING);
    }

    private static class InstanceHolder{
        public static final RedisStringClient CLIENT = new RedisStringClient();
    }

    public static RedisStringClient getInstance(){
        return InstanceHolder.CLIENT;
    }

    @Override
    public boolean set(String key, String v){
        SetStringCmd setCmd = new SetStringCmd(key, v);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setNx(String key, String v) {
        SetStringCmd setCmd = new SetStringCmd(key, v, Xmode.NX);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setWithExpireTime(String key, String v, long seconds) {
        SetStringCmd setCmd = new SetStringCmd(key, v,ExpireMode.EX, seconds);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean set(String key, String v, ExpireMode expireMode, long expireTime, Xmode x) {
        SetStringCmd setCmd = new SetStringCmd(key, v,ExpireMode.EX, expireTime,  Xmode.NX);
        try {
            return invokeCmd(setCmd, setCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String get(String key){
        GetStringCmd getStringCmd = new GetStringCmd(key);
        try {
            return  invokeCmd(getStringCmd, getStringCmd);
        } catch (FailedToGetConnectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
