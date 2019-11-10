package com.north.netty.kafka.bean.produce;

import com.north.netty.kafka.bean.msg.KafkaMsgRecordBatch;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class Record implements Serializable {
    private Integer partition;
    private KafkaMsgRecordBatch kafkaMsgRecordBatch;
    public void serializable(ByteBuf out){
        out.writeInt(partition);
        if(kafkaMsgRecordBatch == null){
            out.writeInt(-1);
        }else{
            out.writeInt(kafkaMsgRecordBatch.getTotalSize());
            kafkaMsgRecordBatch.serializable(out);
        }

    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public KafkaMsgRecordBatch getKafkaMsgRecordBatch() {
        return kafkaMsgRecordBatch;
    }

    public void setKafkaMsgRecordBatch(KafkaMsgRecordBatch kafkaMsgRecordBatch) {
        this.kafkaMsgRecordBatch = kafkaMsgRecordBatch;
    }
}
