package com.north.netty.kafka.bean.msg;

import com.google.common.collect.Lists;
import com.north.netty.kafka.utils.Crc32C;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KafkaMsgRecordBatch implements Serializable {
    private int totalSize;
    public KafkaMsgRecordBatch(){

    }
    public KafkaMsgRecordBatch(KafkaMsgRecordV2 kafkaMsgRecordV2){
        this.msgs = Lists.newArrayList(kafkaMsgRecordV2);
        this.baseOffset = 0L;
        this.firstTimestamp = this.maxTimestamp = System.currentTimeMillis();
        /**
         *  61 是KafkaMsgRecordBatch整个消息头部的大小, 12是baseOffset和length的长度和
         *  KafkaMsgRecordBatch的长度就是 头部+消息体-12
         */
        this.length = 61 + kafkaMsgRecordV2.getMsgSize() - 12;
        this.totalSize = 61 + kafkaMsgRecordV2.getMsgSize();
    }

    public void deserialize(ByteBuf in){
        this.baseOffset = in.readLong();
        this.length = in.readInt();
        this.partitionLeaderVersion = in.readInt();
        this.magic = in.readByte();
        this.crc = in.readInt();
        this.attributes = in.readShort();
        this.lastOffsetDelta = in.readInt();
        this.firstTimestamp = in.readLong();
        this.maxTimestamp = in.readLong();
        this.producerId = in.readLong();
        this.epoch = in.readShort();
        this.sequence = in.readInt();
        this.numRecords = in.readInt();
        if(numRecords >= 0){
            msgs = new ArrayList<>(numRecords);
            for(int i=0; i<numRecords; i++){
                KafkaMsgRecordV2 kafkaMsgRecordV2 = new KafkaMsgRecordV2();
                kafkaMsgRecordV2.deserialize(in);
                msgs.add(kafkaMsgRecordV2);
            }
        }
    }
    public void serializable(ByteBuf out){
        out.writeLong(this.baseOffset);
        out.writeInt(this.length);
        out.writeInt(this.partitionLeaderVersion);
        out.writeByte(magic);

        ByteBuf tmp = PooledByteBufAllocator.DEFAULT.buffer();

        /***这部分要计算crc 先临时存到一个buf***/
        tmp.writeShort(this.attributes);
        tmp.writeInt(this.lastOffsetDelta);
        tmp.writeLong(this.firstTimestamp);
        tmp.writeLong(this.maxTimestamp);
        tmp.writeLong(producerId);
        tmp.writeShort(epoch);
        tmp.writeInt(sequence);
        tmp.writeInt(numRecords);
        for(KafkaMsgRecordV2 kafkaMsgRecordV2 : msgs){
            kafkaMsgRecordV2.serializable(tmp);
        }
        /******/
        // 计算crc 并写到buf
        calcCrc32(tmp);
        out.writeInt(this.crc);

        /**
         * 把临时buf的数据写进去
         */
        out.writeBytes(tmp);

    }
    private void calcCrc32(ByteBuf buf){
        byte [] tmp = new byte[buf.writerIndex()];
        buf.getBytes(0, tmp);

        // 从attributes开始计算
        long crc32 = Crc32C.compute(tmp, 0, tmp.length);
        this.crc = (int) crc32;
    }
    /**
     * 该消息批次的起始位移, 就是第一条消息的位移
     */
    private Long baseOffset;
    /**
     * 该批次的消息的长度 (除了 baseOffset和length以外所有字段的总大小)
     */
    private Integer length;

    /**
     *  分区leader版本号 这里暂时写死为-1
     */
    private Integer partitionLeaderVersion = -1;

    /**
     * 版本号, 我们这里的实现都是V2版本 所以写死为2
     */
    private byte magic = 2;

    /**
     * crc校验码
     */
    private Integer crc;

    /**
     * 属性字段 我们这里没有用压缩 也不涉及事务   所以写死为0
     */
    private short attributes = 0;

    /**
     * 最大位移增量   即最后一条消息与baseOffset的差值
     * 我们这里的实现是每个批次只有一条消息   所以这个值固定为0
     */
    private int lastOffsetDelta = 0;

    /**
     * 该批次中第一条消息的时间戳
     */
    private long firstTimestamp;
    /**
     *  该批次中最后一条消息的时间戳
     */
    private long maxTimestamp;

    /**
     * 跟消息幂等性和事务等有关  我们这里没有涉及到  先写死为-1
     */
    private long producerId = -1;
    private short epoch = -1;
    private int sequence = -1;
    /**
     * 该批次中消息的个数, 所以我们还是暂时可以写死为1
     */
    private int  numRecords = 1;

    /**
     * 消息体
     */
    private List<KafkaMsgRecordV2> msgs;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public Long getBaseOffset() {
        return baseOffset;
    }

    public void setBaseOffset(Long baseOffset) {
        this.baseOffset = baseOffset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPartitionLeaderVersion() {
        return partitionLeaderVersion;
    }

    public void setPartitionLeaderVersion(Integer partitionLeaderVersion) {
        this.partitionLeaderVersion = partitionLeaderVersion;
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public Integer getCrc() {
        return crc;
    }

    public void setCrc(Integer crc) {
        this.crc = crc;
    }

    public short getAttributes() {
        return attributes;
    }

    public void setAttributes(short attributes) {
        this.attributes = attributes;
    }

    public int getLastOffsetDelta() {
        return lastOffsetDelta;
    }

    public void setLastOffsetDelta(int lastOffsetDelta) {
        this.lastOffsetDelta = lastOffsetDelta;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public short getEpoch() {
        return epoch;
    }

    public void setEpoch(short epoch) {
        this.epoch = epoch;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

    public List<KafkaMsgRecordV2> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<KafkaMsgRecordV2> msgs) {
        this.msgs = msgs;
    }
}
