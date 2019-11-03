package org.nort.netty.redis.test;


import com.north.netty.redis.RedisClient;
import org.junit.Test;

public class RedisClientTest {
    @Test
    public void testSet() throws InterruptedException {
        RedisClient redisClient = new RedisClient("localhost", 6379);
        Thread.sleep(2000);
        redisClient.set("lhh", "lhh");
        Thread.sleep(1000000);
    }
}
