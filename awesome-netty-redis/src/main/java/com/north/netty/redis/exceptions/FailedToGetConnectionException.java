package com.north.netty.redis.exceptions;

/**
 * @author laihaohua
 */
public class FailedToGetConnectionException extends AwesomeNettyRedisException {
    public FailedToGetConnectionException(String message) {
        super(message);
    }
}
