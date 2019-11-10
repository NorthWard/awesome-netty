package com.north.netty.kafka.bean.meta;

import com.north.netty.kafka.bean.KafkaRequest;
import com.north.netty.kafka.bean.KafkaRequestHeader;
import com.north.netty.kafka.enums.ApiKeys;
import io.netty.buffer.ByteBuf;
import org.north.netty.common.utils.SerializeUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author laihaohua
 */
public class KafkaMetaRequest  implements Serializable, KafkaRequest {
    private List<String> topics;
    private boolean allowAutoTopicCreation = true;
    private KafkaRequestHeader header;
    public KafkaMetaRequest(String clientId, Integer correlationId){
        super();
        header = new KafkaRequestHeader();
        header.setClientId(clientId);
        header.setCorrelationId(correlationId);
        header.setApiKey(ApiKeys.METADATA.id);
        header.setApiVersion(ApiKeys.METADATA.apiVersion);
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

    @Override
    public void serializable(ByteBuf out){
        header.serializable(out);
        SerializeUtils.writeStringListToBuffer(topics, out);
    }
}
