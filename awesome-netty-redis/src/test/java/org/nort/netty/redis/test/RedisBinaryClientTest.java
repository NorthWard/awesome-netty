package org.nort.netty.redis.test;

import com.north.netty.redis.clients.RedisBinaryClient;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.north.netty.common.utils.ProtostuffHelper;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.HashMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisBinaryClientTest {
    public static class Key implements Serializable{
        public Key(){

        }
        private Object key;
        public Key(Object key){
            this.key = key;
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }
    }
    public static class Val implements Serializable{
        public Val(){

        }
        private Object val;
        public Val(Object val){
            this.val = val;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }
    }
    public static final Key KEY = new Key("j");
    public static final Val VAL = new Val("V");
    @Test
    public void test001() throws InterruptedException {
        RedisBinaryClient redisClient = RedisBinaryClient.getInstance();
        boolean b = redisClient.set(ProtostuffHelper.serializeObject(KEY, Key.class), ProtostuffHelper.serializeObject(VAL, Val.class));
        assert b;
    }

    @Test
    public void test002() throws InterruptedException {
        RedisBinaryClient redisClient = RedisBinaryClient.getInstance();
        byte [] bytes = redisClient.get(ProtostuffHelper.serializeObject(KEY, Key.class));
        Val val = ProtostuffHelper.deSerializeObject(bytes, Val.class);
        assert val.getVal().equals(VAL.getVal());
    }
}
