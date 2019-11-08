package com.north.netty.kafka.bean.broker;

import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;

/**
 * @author laihaohua
 */
public class Broker implements Serializable {
    private Integer nodeId;
    private String  host;
    private Integer port;
    private String rack;

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public void deserialize(ByteBuf byteBuf) {
        this.nodeId = byteBuf.readInt();
        this.host = SerializeUtils.readStringToBuffer2(byteBuf);
        this.port = byteBuf.readInt();
        this.rack = SerializeUtils.readStringToBuffer2(byteBuf);
    }
}
