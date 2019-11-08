package com.north.netty.kafka.bean;

import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;

/**
 * @author laihaohua
 */
public class KafkaRequestHeader implements Serializable {
    private Short apiKey;
    private Short apiVersion;
    private Integer correlationId;
    private String clientId;
    public void serializable(ByteBuf out){
        out.writeShort(apiKey);
        out.writeShort(apiVersion);
        out.writeInt(correlationId);
        SerializeUtils.writeStringToBuffer2(clientId, out);
    }

    public Short getApiKey() {
        return apiKey;
    }

    public void setApiKey(Short apiKey) {
        this.apiKey = apiKey;
    }

    public Short getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(Short apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Integer correlationId) {
        this.correlationId = correlationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
