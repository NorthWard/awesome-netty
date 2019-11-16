package com.north.netty.kafka.bean.msg;

import com.north.netty.kafka.utils.VarLengthUtils;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laihaohua
 */
public class KafkaMsgRecordV2 implements Serializable {

    private int msgBodySize;

    public KafkaMsgRecordV2(){

    }
    public KafkaMsgRecordV2(byte [] key, byte [] values, Map<String, byte[]> headers){
        this.key = key;
        this.values = values;
        if (headers == null){
            headers = new HashMap<>();
        }
        this.headers = headers;
        calcMsgSize();

    }
    private void calcMsgSize(){
        int size = 0;
        // attributes属性的大小
        size += 1;
        // offsetDelta的大小
        size += VarLengthUtils.sizeOfVarint(this.offsetDelta);
        // timestampDelta的大小
        size += VarLengthUtils.sizeOfVarlong(this.timestampDelta);
        if(this.key == null){
            // -1 占1位
            size += VarLengthUtils.NULL_VARINT_SIZE_BYTES;
        }else{
            size += VarLengthUtils.sizeOfVarint(this.key.length);
            size += this.key.length;
        }
        if(this.values == null){
            // -1 占1位
            size += VarLengthUtils.NULL_VARINT_SIZE_BYTES;
        }else{
            size += VarLengthUtils.sizeOfVarint(this.values.length);
            size += this.values.length;
        }
        size += VarLengthUtils.sizeOfVarint(headers.size());
        for (Map.Entry<String, byte[]> header : headers.entrySet()) {
            String headerKey = header.getKey();
            if (headerKey == null) {
                throw new IllegalArgumentException("Invalid null header key found in headers");
            }

            int headerKeySize = VarLengthUtils.utf8Length(headerKey);
            size += VarLengthUtils.sizeOfVarint(headerKeySize) + headerKeySize;

            byte[] headerValue = header.getValue();
            if (headerValue == null) {
                size += VarLengthUtils.NULL_VARINT_SIZE_BYTES;
            } else {
                size += VarLengthUtils.sizeOfVarint(headerValue.length) + headerValue.length;
            }
        }

        this.msgBodySize = size;
        this.msgSize = VarLengthUtils.sizeOfVarint(size) + size;
    }
    public void serializable(ByteBuf out){
        VarLengthUtils.writeVarint(msgBodySize, out);
        out.writeByte(attributes);
        VarLengthUtils.writeVarlong(timestampDelta, out);
        VarLengthUtils.writeVarint(offsetDelta, out);
        if(key == null){
            VarLengthUtils.writeVarint(-1, out);
        }else{
            VarLengthUtils.writeVarint(key.length, out);
            out.writeBytes(key);
        }
        if(values == null){
            VarLengthUtils.writeVarint(-1, out);
        }else{
            VarLengthUtils.writeVarint(values.length, out);
            out.writeBytes(values);
        }

        VarLengthUtils.writeVarint(headers.size(), out);

        for (Map.Entry<String, byte[]> header : headers.entrySet()) {
            String headerKey = header.getKey();
            if (headerKey == null) {
                throw new IllegalArgumentException("Invalid null header key found in headers");
            }

            byte[] utf8Bytes = headerKey.getBytes(StandardCharsets.UTF_8);
            VarLengthUtils.writeVarint(utf8Bytes.length, out);
            out.writeBytes(utf8Bytes);

            byte[] headerValue = header.getValue();
            if (headerValue == null) {
                VarLengthUtils.writeVarint(-1, out);
            } else {
                VarLengthUtils.writeVarint(headerValue.length, out);
                out.writeBytes(headerValue);
            }
        }

    }
    public void deserialize(ByteBuf in){
       this.msgBodySize = VarLengthUtils.readVarint(in);
       this.attributes = in.readByte();
       timestampDelta = VarLengthUtils.readVarlong(in);
       offsetDelta = VarLengthUtils.readVarint(in);
       int keyLen = VarLengthUtils.readVarint(in);
       if(keyLen >= 0){
           this.key = new byte[keyLen];
           in.readBytes(key);
       }

       int valueLen = VarLengthUtils.readVarint(in);
        if(valueLen >= 0){
            this.values = new byte[valueLen];
            in.readBytes(values);
        }
       int headerSize = VarLengthUtils.readVarint(in);
        if(headerSize >= 0){
            headers = new HashMap<>(headerSize);
            for(int i=0; i < headerSize; i++){
                keyLen = VarLengthUtils.readVarint(in);
                byte [] bs = new byte[keyLen];
                in.readBytes(bs);
                String key = new String(bs);
                valueLen = VarLengthUtils.readVarint(in);
                bs = new byte[valueLen];
                in.readBytes(bs);
                headers.put(key, bs);
            }
        }
    }
    /**
     * 消息总长度
     */
    private int msgSize;
    /**
     * 1字节的属性 我们这里没有用到压缩 所以写死为0
     */
    private byte attributes = 0;
    /**
     * 当前消息中的时间戳 与该批次中的第一条消息的时间戳的差值
     * 在我们这里的实现里,每个批次只发送一条消息, 所以这个值固定为0
     */
    private long timestampDelta = 0;

    /**
     * 当前消息的offset与该批次中的第一条消息的offset的差值
     * 在我们这里的实现里,每个批次只发送一条消息, 所以这个值固定为0
     */
    private int offsetDelta = 0;
    /**
     * 该消息的key
     */
    private byte [] key;
    /**
     * 该消息的value
     */
    private byte [] values;

    /**
     * 该消息的headers
     */
    private Map<String, byte[]> headers;


    public int getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    public byte getAttributes() {
        return attributes;
    }

    public void setAttributes(byte attributes) {
        this.attributes = attributes;
    }

    public long getTimestampDelta() {
        return timestampDelta;
    }

    public void setTimestampDelta(long timestampDelta) {
        this.timestampDelta = timestampDelta;
    }

    public int getOffsetDelta() {
        return offsetDelta;
    }

    public void setOffsetDelta(int offsetDelta) {
        this.offsetDelta = offsetDelta;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }

    public Map<String, byte[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, byte[]> headers) {
        this.headers = headers;
    }
}
