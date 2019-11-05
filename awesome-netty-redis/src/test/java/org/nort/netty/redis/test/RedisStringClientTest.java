package org.nort.netty.redis.test;


import com.north.netty.redis.clients.RedisStringClient;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisStringClientTest {
    public static final String KEY = "TEST";
    public static final String VAL = "YES";
    @Test
    public void test001() throws InterruptedException {
        RedisStringClient redisStringClient = RedisStringClient.getInstance();
        boolean b  = redisStringClient.set(KEY, VAL);
        assert b;
    }



    @Test
    public void test002() throws InterruptedException {
        RedisStringClient redisStringClient = RedisStringClient.getInstance();
        String str = redisStringClient.get(KEY);
        assert  VAL.equalsIgnoreCase(str);
    }




}
