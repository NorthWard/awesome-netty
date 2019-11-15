package com.north.netty.kafka.bean.produce;

import com.north.netty.kafka.bean.AbstractKafkaResponse;
import com.north.netty.kafka.bean.KafkaResponse;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProduceResponse  extends AbstractKafkaResponse implements Serializable, KafkaResponse {
    private List<TopicProduceRes> topicProduceResList;
    private Integer throttleTimeMs;

    @Override
    public void deserialize(ByteBuf byteBuf) {
       int topicCount = byteBuf.readInt();
       if(topicCount >= 0){
           topicProduceResList = new ArrayList<>(topicCount);
           for(int i=0; i < topicCount; i++){
               TopicProduceRes topicProduceRes = new TopicProduceRes();
               topicProduceRes.deserialize(byteBuf);
               topicProduceResList.add(topicProduceRes);
           }
       }
       this.throttleTimeMs = byteBuf.readInt();
    }
}
