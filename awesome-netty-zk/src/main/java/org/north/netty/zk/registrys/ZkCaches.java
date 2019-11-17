package org.north.netty.zk.registrys;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.ZkResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 一个缓存中心, 保存zkClient实例中每个xid对应的codec
 * @author laihaohua
 */
public final class ZkCaches {
    private  final Cache<Integer, ByteToMessageCodec> codecMap  = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();
    private  final Cache<Integer, ZkResponse> respMap  = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public  void putCodec(Integer requestId, ByteToMessageCodec codec){
        codecMap.put(requestId, codec);
    }

    public  ByteToMessageCodec getCodec(Integer requestId){
        return codecMap.getIfPresent(requestId);
    }

    public  void removeCodec(Integer requestId){
        codecMap.invalidate(requestId);
    }
    public  void putResp(Integer requestId, ZkResponse zkResponse){
        respMap.put(requestId, zkResponse);
    }

    public  ZkResponse getResp(Integer requestId, long duration, TimeUnit unit) throws TimeoutException {
        long t = System.currentTimeMillis();
        long timeout = TimeUnit.MILLISECONDS.convert(duration, unit);
        ZkResponse response = null;
        while((response = respMap.getIfPresent(requestId)) == null){
            long t2 = System.currentTimeMillis();
            if(t2 > (t + timeout)){
                  // remove when timeout
                  removeResp(requestId);
                 throw new TimeoutException("get resp timeout after " + timeout + " ms ");
            }
        }
        return response;
    }

    public  void removeResp(Integer requestId){
        respMap.invalidate(requestId);
    }
}
