package com.north.netty.kafka.bean.produce;

import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.List;

public class TopicProduceData implements Serializable {
    private String topic;
    private List<PartitionData> data;
    public void serializable(ByteBuf out){
        SerializeUtils.writeStringToBuffer2(topic, out);
        if(data == null){
            out.writeInt(-1);
        }else{
            out.writeInt(data.size());
            for(PartitionData partitionData : data){
                partitionData.serializable(out);
            }
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<PartitionData> getData() {
        return data;
    }

    public void setData(List<PartitionData> data) {
        this.data = data;
    }
}
