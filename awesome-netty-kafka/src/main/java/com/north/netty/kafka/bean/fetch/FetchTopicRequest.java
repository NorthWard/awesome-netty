package com.north.netty.kafka.bean.fetch;

import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.List;

public class FetchTopicRequest implements Serializable {
    private String topic;
    private List<FetchTopicPartitionRequest> partitions;
    public void serializable(ByteBuf out){
        SerializeUtils.writeStringToBuffer2(topic, out);
        if(partitions == null){
          out.writeInt(-1);
        }else {
          out.writeInt(partitions.size());
          for(FetchTopicPartitionRequest fetchTopicRequest : partitions){
               fetchTopicRequest.serializable(out);
          }
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<FetchTopicPartitionRequest> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<FetchTopicPartitionRequest> partitions) {
        this.partitions = partitions;
    }
}
