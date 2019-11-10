package com.north.netty.kafka.bean.fetch;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class FetchTopicPartitionRequest implements Serializable {
    private Integer partition;
    private Long fetchOffset;
    private Long logStartOffset;
    private Integer maxBytes;
    public void serializable(ByteBuf out){
        out.writeInt(partition);
        out.writeLong(fetchOffset);
        out.writeLong(logStartOffset);
        out.writeInt(maxBytes);
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public Long getFetchOffset() {
        return fetchOffset;
    }

    public void setFetchOffset(Long fetchOffset) {
        this.fetchOffset = fetchOffset;
    }

    public Long getLogStartOffset() {
        return logStartOffset;
    }

    public void setLogStartOffset(Long logStartOffset) {
        this.logStartOffset = logStartOffset;
    }

    public Integer getMaxBytes() {
        return maxBytes;
    }

    public void setMaxBytes(Integer maxBytes) {
        this.maxBytes = maxBytes;
    }
}
