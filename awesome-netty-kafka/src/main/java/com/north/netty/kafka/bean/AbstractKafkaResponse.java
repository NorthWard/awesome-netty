package com.north.netty.kafka.bean;

/**
 * @author laihaohua
 */
public abstract class AbstractKafkaResponse implements  KafkaResponse{
    protected  Integer correlationId;
    protected KafkaResponseHeader kafkaResponseHeader;

    public KafkaResponseHeader getKafkaResponseHeader() {
        return kafkaResponseHeader;
    }

    public void setKafkaResponseHeader(KafkaResponseHeader kafkaResponseHeader) {
        this.kafkaResponseHeader = kafkaResponseHeader;
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Integer correlationId) {
        this.correlationId = correlationId;
    }
}
