package com.north.netty.kafka.bean;

import com.north.netty.kafka.bean.broker.Broker;
import com.north.netty.kafka.bean.topic.TopicMetaData;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class KafkaMetaResponse extends KafkaResponseHeader {
    private Integer throttleTimeMs;
    List<Broker> brokers;
    private Integer controllerId = -1;
    /**
     *  v2+的版本才有这个字段
     */
    private String clusterId;
    private  List<TopicMetaData> topicMetadata;

    @Override
    public void deserialize(ByteBuf byteBuf) {
        super.deserialize(byteBuf);
        //this.throttleTimeMs = byteBuf.readInt();
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
