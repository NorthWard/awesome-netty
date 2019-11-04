package org.nort.netty.redis.test;

import com.north.netty.redis.RedisBinaryClient;
import org.junit.Test;

public class RedisBinaryClientTest {
    @Test
    public void testSet2() throws InterruptedException {
        RedisBinaryClient redisClient = new RedisBinaryClient("localhost", 6379);
        Thread.sleep(2000);
        redisClient.set("lhhb".getBytes(), "lhhb".getBytes());
        Thread.sleep(1000000);
    }

    @Test
    public void testGet2() throws InterruptedException {
        RedisBinaryClient redisClient = new RedisBinaryClient("localhost", 6379);
        Thread.sleep(2000);
        redisClient.get("lhhb".getBytes());
        Thread.sleep(1000000);
    }
}
