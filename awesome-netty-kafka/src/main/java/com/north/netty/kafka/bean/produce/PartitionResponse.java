package com.north.netty.kafka.bean.produce;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class PartitionResponse  implements Serializable {
    private Integer partitionId;
    private Short errorCode;
    private Long baseOffset;
    private Long logAppendTime;
    private Long logStartOffset;
    public void deserialize(ByteBuf byteBuf) {
        this.partitionId = byteBuf.readInt();
        this.errorCode = byteBuf.readShort();
        this.baseOffset = byteBuf.readLong();
        this.logAppendTime = byteBuf.readLong();
        this.logStartOffset = byteBuf.readLong();
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public Short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Short errorCode) {
        this.errorCode = errorCode;
    }

    public Long getBaseOffset() {
        return baseOffset;
    }

    public void setBaseOffset(Long baseOffset) {
        this.baseOffset = baseOffset;
    }

    public Long getLogAppendTime() {
        return logAppendTime;
    }

    public void setLogAppendTime(Long logAppendTime) {
        this.logAppendTime = logAppendTime;
    }

    public Long getLogStartOffset() {
        return logStartOffset;
    }

    public void setLogStartOffset(Long logStartOffset) {
        this.logStartOffset = logStartOffset;
    }
}
