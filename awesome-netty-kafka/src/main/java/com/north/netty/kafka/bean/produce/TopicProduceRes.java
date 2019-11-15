package com.north.netty.kafka.bean.produce;

import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicProduceRes implements Serializable {
    private String topic;
    private List<PartitionResponse> partitionResponseList;
    public void deserialize(ByteBuf byteBuf) {
        this.topic = SerializeUtils.readStringToBuffer2(byteBuf);
        int partitionCount = byteBuf.readInt();
        if(partitionCount >= 0){
            partitionResponseList = new ArrayList<>();
            for(int i = 0; i < partitionCount; i++){
                PartitionResponse partitionResponse = new PartitionResponse();
                partitionResponse.deserialize(byteBuf);
                partitionResponseList.add(partitionResponse);
            }
        }
    }
}
