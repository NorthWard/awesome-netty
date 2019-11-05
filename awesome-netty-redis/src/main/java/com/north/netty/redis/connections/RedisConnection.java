package com.north.netty.redis.connections;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author laihaohua
 */
public class RedisConnection<T>{
    private NioSocketChannel socketChannel;
    private Lock lock = new ReentrantLock();
    private SynchronousQueue<T> synchronousQueue;
    private String name;
    public RedisConnection(String name, NioSocketChannel socketChannel, SynchronousQueue<T> synchronousQueue){
        this.name = name;
           this.socketChannel = socketChannel;
           this.synchronousQueue = synchronousQueue;
    }
    public void cleanUp(){
    }
    public T getResp(long timeout) throws InterruptedException {
        return synchronousQueue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void lock() {
        lock.lock();
    }
    public void unlock() {
         lock.unlock();
    }

    /***********************代理channel的几个方法*******************************/
    public ChannelFuture writeAndFlush(Object msg) {
        return socketChannel.writeAndFlush(msg);
    }

    public void disconnect() {
        socketChannel.disconnect();
    }

    public void close() {
        socketChannel.close();
    }

    public boolean isActive() {
       return socketChannel.isActive();
    }
}
