package com.north.netty.kafka.bean.msg;

import com.north.netty.kafka.ByteUtils;
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
        size += ByteUtils.sizeOfVarint(this.offsetDelta);
        // timestampDelta的大小
        size += ByteUtils.sizeOfVarlong(this.timestampDelta);
        if(this.key == null){
            // -1 占1位
            size += ByteUtils.NULL_VARINT_SIZE_BYTES;
        }else{
            size += ByteUtils.sizeOfVarint(this.key.length);
            size += this.key.length;
        }
        if(this.values == null){
            // -1 占1位
            size += ByteUtils.NULL_VARINT_SIZE_BYTES;
        }else{
            size += ByteUtils.sizeOfVarint(this.values.length);
            size += this.values.length;
        }
        size += ByteUtils.sizeOfVarint(headers.size());
        for (Map.Entry<String, byte[]> header : headers.entrySet()) {
            String headerKey = header.getKey();
            if (headerKey == null) {
                throw new IllegalArgumentException("Invalid null header key found in headers");
            }

            int headerKeySize = ByteUtils.utf8Length(headerKey);
            size += ByteUtils.sizeOfVarint(headerKeySize) + headerKeySize;

            byte[] headerValue = header.getValue();
            if (headerValue == null) {
                size += ByteUtils.NULL_VARINT_SIZE_BYTES;
            } else {
                size += ByteUtils.sizeOfVarint(headerValue.length) + headerValue.length;
            }
        }

        this.msgBodySize = size;
        this.msgSize = ByteUtils.sizeOfVarint(size) + size;
    }
    public void serializable(ByteBuf out){
        ByteUtils.writeVarint(msgBodySize, out);
        out.writeByte(attributes);
        ByteUtils.writeVarlong(timestampDelta, out);
        ByteUtils.writeVarint(offsetDelta, out);
        if(key == null){
            ByteUtils.writeVarint(-1, out);
        }else{
            ByteUtils.writeVarint(key.length, out);
            out.writeBytes(key);
        }
        if(values == null){
            ByteUtils.writeVarint(-1, out);
        }else{
            ByteUtils.writeVarint(values.length, out);
            out.writeBytes(values);
        }

        ByteUtils.writeVarint(headers.size(), out);

        for (Map.Entry<String, byte[]> header : headers.entrySet()) {
            String headerKey = header.getKey();
            if (headerKey == null) {
                throw new IllegalArgumentException("Invalid null header key found in headers");
            }

            byte[] utf8Bytes = headerKey.getBytes(StandardCharsets.UTF_8);
            ByteUtils.writeVarint(utf8Bytes.length, out);
            out.writeBytes(utf8Bytes);

            byte[] headerValue = header.getValue();
            if (headerValue == null) {
                ByteUtils.writeVarint(-1, out);
            } else {
                ByteUtils.writeVarint(headerValue.length, out);
                out.writeBytes(headerValue);
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
