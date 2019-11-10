package com.north.netty.kafka.bean.fetch;


import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchTopicResponse implements Serializable {
    private String topic;
    private List<FetchPartitionResp> partitionResps;

    public void deserialize(ByteBuf in){
        this.topic = SerializeUtils.readStringToBuffer2(in);
        int partitionResCount = in.readInt();
        if(partitionResCount > 0){
            this.partitionResps = new ArrayList<>(partitionResCount);
            for(int i = 0; i < partitionResCount; i++){
                FetchPartitionResp fetchPartitionResp = new FetchPartitionResp();
                fetchPartitionResp.deserialize(in);
                partitionResps.add(fetchPartitionResp);
            }
        }
    }

}
