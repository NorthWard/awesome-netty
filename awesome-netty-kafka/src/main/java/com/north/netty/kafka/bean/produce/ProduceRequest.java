package com.north.netty.kafka.bean.produce;

import com.north.netty.kafka.bean.KafkaRequest;
import com.north.netty.kafka.bean.KafkaRequestHeader;
import com.north.netty.kafka.enums.ApiKeys;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.util.Arrays;
import java.util.List;

public class ProduceRequest implements KafkaRequest {
    private String transactionalId;
    private Short acks;
    private Integer timeOut;
    private List<TopicProduceData> topicData;
    private KafkaRequestHeader requestHeader;
    public ProduceRequest(String clientId, Integer correlationId) {
        super();
        this.requestHeader = new KafkaRequestHeader();
        this.requestHeader.setClientId(clientId);
        this.requestHeader.setCorrelationId(correlationId);
        this.requestHeader.setApiKey(ApiKeys.PRODUCE.id);
        this.requestHeader.setApiVersion(ApiKeys.PRODUCE.apiVersion);

    }

    @Override
    public void serializable(ByteBuf out) {
        requestHeader.serializable(out);
        SerializeUtils.writeStringToBuffer2(transactionalId, out);
        out.writeShort(acks);
        out.writeInt(timeOut);
        if(topicData == null){
            out.writeInt(-1);
        }else {
            out.writeInt(topicData.size());
            for(TopicProduceData topicProduceData: topicData){
                topicProduceData.serializable(out);

            }
        }

    }

    public String getTransactionalId() {
        return transactionalId;
    }

    public void setTransactionalId(String transactionalId) {
        this.transactionalId = transactionalId;
    }

    public Short getAcks() {
        return acks;
    }

    public void setAcks(Short acks) {
        this.acks = acks;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public List<TopicProduceData> getTopicData() {
        return topicData;
    }

    public void setTopicData(List<TopicProduceData> topicData) {
        this.topicData = topicData;
    }
}
