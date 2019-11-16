package com.north.netty.kafka.bean.fetch;

import com.north.netty.kafka.bean.produce.Record;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FetchPartitionResp implements Serializable {
    private FetchPartitionHeader partitionHeaders;
    private Record recordSset;
    public void deserialize(ByteBuf in){
        partitionHeaders = new FetchPartitionHeader();
        partitionHeaders.deserialize(in);
        recordSset = new Record();
        recordSset.deserialize(in);
    }

    public FetchPartitionHeader getPartitionHeaders() {
        return partitionHeaders;
    }

    public void setPartitionHeaders(FetchPartitionHeader partitionHeaders) {
        this.partitionHeaders = partitionHeaders;
    }

    public Record getRecordSset() {
        return recordSset;
    }

    public void setRecordSset(Record recordSset) {
        this.recordSset = recordSset;
    }
}
