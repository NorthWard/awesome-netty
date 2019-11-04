package org.nort.netty.redis.test;


import com.north.netty.redis.RedisStringClient;
import org.junit.Test;

public class RedisStringClientTest {
    @Test
    public void testSet() throws InterruptedException {
        RedisStringClient redisClient = new RedisStringClient("localhost", 6379);
        Thread.sleep(2000);
        redisClient.set("lhh", "lhh");
        Thread.sleep(1000000);
    }



    @Test
    public void testGet() throws InterruptedException {
        RedisStringClient redisClient = new RedisStringClient("localhost", 6379);
        Thread.sleep(2000);
        redisClient.get("lhh");
        Thread.sleep(1000000);
    }
}
