package com.north.netty.kafka.bean.fetch;

import com.north.netty.kafka.bean.produce.Record;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchPartitionResp implements Serializable {
    private List<FetchPartitionHeader> partitionHeaders;
    private Record recordSset;
    public void deserialize(ByteBuf in){
        int partitonHeaderConut = in.readInt();
        if(partitonHeaderConut > 0){
            this.partitionHeaders = new ArrayList<>(partitonHeaderConut);
            for(int i = 0; i < partitonHeaderConut; i++){
                FetchPartitionHeader fetchPartitionHeader = new FetchPartitionHeader();
                fetchPartitionHeader.deserialize(in);
                partitionHeaders.add(fetchPartitionHeader);
            }
        }
    }
}
