package com.north.netty.kafka.bean;

import java.io.Serializable;

public class KafkaRequest implements Serializable {
    private Short apiKey;
    private Short apiVersion;
    private Integer correlationId;
    private String clientId;
}
