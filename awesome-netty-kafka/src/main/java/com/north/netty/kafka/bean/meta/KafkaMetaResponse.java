package com.north.netty.kafka.bean.meta;

import com.north.netty.kafka.bean.AbstractKafkaResponse;
import com.north.netty.kafka.bean.KafkaResponse;
import com.north.netty.kafka.bean.broker.Broker;
import com.north.netty.kafka.bean.topic.TopicMetaData;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class KafkaMetaResponse extends AbstractKafkaResponse implements Serializable, KafkaResponse {
    private Integer throttleTimeMs;
    private List<Broker> brokers;
    private Integer controllerId = -1;
    /**
     *  v2+的版本才有这个字段
     */
    private String clusterId;
    private  List<TopicMetaData> topicMetadata;


    public Integer getThrottleTimeMs() {
        return throttleTimeMs;
    }

    public void setThrottleTimeMs(Integer throttleTimeMs) {
        this.throttleTimeMs = throttleTimeMs;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<Broker> brokers) {
        this.brokers = brokers;
    }

    public Integer getControllerId() {
        return controllerId;
    }

    public void setControllerId(Integer controllerId) {
        this.controllerId = controllerId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public List<TopicMetaData> getTopicMetadata() {
        return topicMetadata;
    }

    public void setTopicMetadata(List<TopicMetaData> topicMetadata) {
        this.topicMetadata = topicMetadata;
    }

    @Override
    public void deserialize(ByteBuf byteBuf) {
        int brokerIdCount = byteBuf.readInt();
        if(brokerIdCount >= 0){
            brokers = new ArrayList<>(brokerIdCount);
            for(int i =0 ; i < brokerIdCount; i++){
                Broker broker = new Broker();
                broker.deserialize(byteBuf);
                brokers.add(broker);
            }
        }
        this.controllerId = byteBuf.readInt();
        int topicCount = byteBuf.readInt();
        if(topicCount >= 0){
            topicMetadata = new ArrayList<>(topicCount);
            for(int i =0 ; i < topicCount; i++){
                TopicMetaData topicMetaData = new TopicMetaData();
                topicMetaData.deserialize(byteBuf);
                topicMetadata.add(topicMetaData);
            }
        }
    }


}
