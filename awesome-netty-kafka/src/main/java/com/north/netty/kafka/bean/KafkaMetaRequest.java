package com.north.netty.kafka.bean;

import com.north.netty.kafka.enums.ApiKeys;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author laihaohua
 */
public class KafkaMetaRequest extends KafkaRequestHeader implements Serializable {
    private List<String> topics;
    private boolean allowAutoTopicCreation = true;
    private short version = 1;
    public KafkaMetaRequest(String clientId, Integer correlationId){
        super();
        super.setClientId(clientId);
        super.setCorrelationId(correlationId);
        super.setApiKey(ApiKeys.METADATA.id);
        super.setApiVersion(version);
    }
    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public boolean isAllowAutoTopicCreation() {
        return allowAutoTopicCreation;
    }

    public void setAllowAutoTopicCreation(boolean allowAutoTopicCreation) {
        this.allowAutoTopicCreation = allowAutoTopicCreation;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    @Override
    public void serializable(ByteBuf out){
        super.serializable(out);
        SerializeUtils.writeStringListToBuffer(topics, out);
    }
}
