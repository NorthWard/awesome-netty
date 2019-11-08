package com.north.netty.kafka.bean.partition;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class PartitionMateData implements Serializable {
    private Short errorCode;
    private Integer partitionId;
    private Integer leader;
    private List<Integer> replicas;
    private List<Integer> isr;
    /**
     * v2版本才有offline_replicas
     */
    private List<Integer> offlineReplicas;

    public Short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Short errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public List<Integer> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<Integer> replicas) {
        this.replicas = replicas;
    }

    public List<Integer> getIsr() {
        return isr;
    }

    public void setIsr(List<Integer> isr) {
        this.isr = isr;
    }

    public List<Integer> getOfflineReplicas() {
        return offlineReplicas;
    }

    public void setOfflineReplicas(List<Integer> offlineReplicas) {
        this.offlineReplicas = offlineReplicas;
    }

    public void deserialize(ByteBuf byteBuf) {
        this.errorCode = byteBuf.readShort();
        this.partitionId = byteBuf.readInt();
        this.leader = byteBuf.readInt();
        int replicasCount = byteBuf.readInt();
        if(replicasCount >= 0){
            replicas = new ArrayList<>(replicasCount);
            for(int i=0; i< replicasCount; i++){
                replicas.add(byteBuf.readInt());
            }
        }
        int isrCount = byteBuf.readInt();
        if(isrCount >= 0){
            isr = new ArrayList<>(isrCount);
            for(int i=0; i< isrCount; i++){
                isr.add(byteBuf.readInt());
            }
        }
    }
}
