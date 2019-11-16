package com.north.netty.kafka.bean.fetch;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchPartitionHeader implements Serializable {
    private Integer partition;
    private Short errorCode;
    private Long highWaterMark;
    private Long lastStableOffset;
    private Long logStartOffset;
    private List<AbortedTransaction> abortedTransactions;
    public void deserialize(ByteBuf in){
        this.partition = in.readInt();
        this.errorCode = in.readShort();
        this.highWaterMark = in.readLong();
        this.lastStableOffset = in.readLong();
        this.logStartOffset = in.readLong();
        int count = in.readInt();
        if(count >= 0){
            abortedTransactions = new ArrayList<>(count);
            for(int i =0 ; i < count; i++){
                AbortedTransaction abortedTransaction = new AbortedTransaction();
                abortedTransaction.deserialize(in);
                abortedTransactions.add(abortedTransaction);
            }
        }

    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public Short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Short errorCode) {
        this.errorCode = errorCode;
    }

    public Long getHighWaterMark() {
        return highWaterMark;
    }

    public void setHighWaterMark(Long highWaterMark) {
        this.highWaterMark = highWaterMark;
    }

    public Long getLastStableOffset() {
        return lastStableOffset;
    }

    public void setLastStableOffset(Long lastStableOffset) {
        this.lastStableOffset = lastStableOffset;
    }

    public Long getLogStartOffset() {
        return logStartOffset;
    }

    public void setLogStartOffset(Long logStartOffset) {
        this.logStartOffset = logStartOffset;
    }

    public List<AbortedTransaction> getAbortedTransactions() {
        return abortedTransactions;
    }

    public void setAbortedTransactions(List<AbortedTransaction> abortedTransactions) {
        this.abortedTransactions = abortedTransactions;
    }
}
