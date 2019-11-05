package com.north.netty.redis.clients;

import com.north.netty.redis.cmd.Cmd;
import com.north.netty.redis.cmd.CmdResp;
import com.north.netty.redis.config.RedisConfig;
import com.north.netty.redis.connections.ConnectionPool;
import com.north.netty.redis.connections.RedisConnection;
import com.north.netty.redis.enums.ClientType;
import com.north.netty.redis.exceptions.FailedToGetConnectionException;

/**
 * @author laihaohua
 */
public abstract class AbstractRedisClient<T> implements RedisClient<T> {
    private ConnectionPool<T> connectionPool;

    protected AbstractRedisClient(ClientType clientType){
          connectionPool = new ConnectionPool<>(clientType);
    }


    protected <RETURN> RETURN invokeCmd(Cmd<T> cmd, CmdResp<T, RETURN> cmdResp) throws FailedToGetConnectionException{
        RedisConnection<T> connection = null;
        try{
            T data = cmd.build();
            connection = connectionPool.borrowConnection();
            if(connectionPool.checkChannel(connection)){
                connection.lock();
                try{
                    connection.writeAndFlush(data).sync();
                    return cmdResp.parseResp(connection.getResp(RedisConfig.TIME_OUT_MS));
                }catch (Exception e){
                   e.printStackTrace();
                }finally {
                    connection.unlock();
                }
            }else{
                throw new FailedToGetConnectionException("can not get connection form connection pool");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connectionPool.checkChannel(connection)){
                connectionPool.returnConnection(connection);
            }
        }
        return null;
    }
}
