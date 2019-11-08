package com.north.netty.kafka.bean.topic;

import com.north.netty.kafka.bean.partition.PartitionMateData;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class TopicMetaData implements Serializable {
    private Short errorCode;
    private String topicName;
    /**
     * 是否是内部的topic
     */
    private Boolean isInternal;

    private List<PartitionMateData> partitionMateDataList;


    public Short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Short errorCode) {
        this.errorCode = errorCode;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Boolean getInternal() {
        return isInternal;
    }

    public void setInternal(Boolean internal) {
        isInternal = internal;
    }

    public List<PartitionMateData> getPartitionMateDataList() {
        return partitionMateDataList;
    }

    public void setPartitionMateDataList(List<PartitionMateData> partitionMateDataList) {
        this.partitionMateDataList = partitionMateDataList;
    }

    public void deserialize(ByteBuf byteBuf) {
        this.errorCode = byteBuf.readShort();
        this.topicName = SerializeUtils.readStringToBuffer2(byteBuf);
        this.isInternal = byteBuf.readBoolean();
        int count = byteBuf.readInt();
        if(count >= 0){
            partitionMateDataList = new ArrayList<>(count);
            for(int i=0; i < count; i++){
                PartitionMateData partitionMateData = new PartitionMateData();
                partitionMateData.deserialize(byteBuf);
                partitionMateDataList.add(partitionMateData);
            }
        }
    }
}
