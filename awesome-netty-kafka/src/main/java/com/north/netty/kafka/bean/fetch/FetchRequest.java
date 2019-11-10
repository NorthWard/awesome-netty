package com.north.netty.kafka.bean.fetch;

import com.north.netty.kafka.bean.KafkaRequest;
import com.north.netty.kafka.bean.KafkaRequestHeader;
import com.north.netty.kafka.enums.ApiKeys;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.List;

public class FetchRequest implements Serializable, KafkaRequest {

    private KafkaRequestHeader header;
    public FetchRequest(String clientId, Integer correlationId){
        super();
        header = new KafkaRequestHeader();
        header.setClientId(clientId);
        header.setCorrelationId(correlationId);
        header.setApiKey(ApiKeys.FETCH.id);
        header.setApiVersion(ApiKeys.FETCH.apiVersion);
    }

    /**
     * 副本的brokerId, 一般的消费者的话 直接用-1即可
     */
    private Integer replicaId;
    /**
     * 等待响应返回的最大ms
     */
    private Integer maxWaitTime;
    /**
     * 响应的最小字节数
     */
    private Integer minBytes;
    /**
     * 响应的最大字节数
     */
    private Integer maxBytes;
    /**
     * 事务隔离等级 0 读未提交  1 读已提交
     */
    private Byte  isolationLevel;
    /**
     * 要拉取的topic
     */
    private List<FetchTopicRequest> topics;

    @Override
    public void serializable(ByteBuf out){
        header.serializable(out);
        out.writeInt(replicaId);
        out.writeInt(maxWaitTime);
        out.writeInt(minBytes);
        out.writeInt(maxBytes);
        out.writeByte(isolationLevel);
        if(topics == null){
            out.writeInt(-1);
        }else {
            out.writeInt(topics.size());
            for(FetchTopicRequest fetchTopicRequest : topics){
                fetchTopicRequest.serializable(out);
            }
        }
    }

}
