package com.north.netty.kafka.bean.produce;

import com.google.gson.Gson;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordBatch;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Record implements Serializable {
    private Integer partition;
    private List<KafkaMsgRecordBatch> kafkaMsgRecordBatchList;

    public void serializable(ByteBuf out){
        out.writeInt(partition);
        if(kafkaMsgRecordBatchList != null){
            for(KafkaMsgRecordBatch kafkaMsgRecordBatch : kafkaMsgRecordBatchList){
                if(kafkaMsgRecordBatch == null){
                    out.writeInt(-1);
                }else{
                    out.writeInt(kafkaMsgRecordBatch.getTotalSize());
                    kafkaMsgRecordBatch.serializable(out);
                }
            }
        }


    }
    public void deserialize(ByteBuf in){
        int totalSize = in.readInt();
        kafkaMsgRecordBatchList = new ArrayList<>();
        while(in.readerIndex() != in.writerIndex()){
            KafkaMsgRecordBatch kafkaMsgRecordBatch = new KafkaMsgRecordBatch();
            kafkaMsgRecordBatch.deserialize(in);
            kafkaMsgRecordBatchList.add(kafkaMsgRecordBatch);
        }
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public List<KafkaMsgRecordBatch> getKafkaMsgRecordBatchList() {
        return kafkaMsgRecordBatchList;
    }

    public void setKafkaMsgRecordBatchList(List<KafkaMsgRecordBatch> kafkaMsgRecordBatchList) {
        this.kafkaMsgRecordBatchList = kafkaMsgRecordBatchList;
    }
}
