package com.north.netty.kafka.caches;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.north.netty.kafka.bean.AbstractKafkaResponse;

import java.util.concurrent.TimeUnit;

/**
 * @author laihaohua
 */
public final class RequestCacheCenter {
    private static Cache<Integer, AbstractKafkaResponse> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public static void putKafkaResponse(Integer correlationId, AbstractKafkaResponse kafkaResponse){
        cache.put(correlationId, kafkaResponse);
    }

    public static AbstractKafkaResponse getKafkaResponse(Integer correlationId){
        return cache.getIfPresent(correlationId);
    }

    public static AbstractKafkaResponse waitForResp(Integer correlationId, long timeoutMs){
        long bt = System.currentTimeMillis();
        AbstractKafkaResponse  response =  null;
        while(response == null || response.getCorrelationId() == null){
            long et = System.currentTimeMillis();
            if(et > bt + timeoutMs){
                return null;
            }
            response =  cache.getIfPresent(correlationId);
        }
        return response;
    }


}
