package com.north.netty.kafka.bean.fetch;

import com.north.netty.kafka.bean.AbstractKafkaResponse;
import com.north.netty.kafka.bean.KafkaResponse;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchResponse  extends AbstractKafkaResponse implements Serializable, KafkaResponse {
    /**
     * ms
     */
    private Integer throttleTime;
    private List<FetchTopicResponse> responses;

    @Override
    public void deserialize(ByteBuf in) {
        this.throttleTime = in.readInt();
        int resCount = in.readInt();
        if(resCount >= 0){
            this.responses = new ArrayList<>(resCount);
            for(int i =0 ; i < resCount; i++){
                FetchTopicResponse fetchTopicResponse = new FetchTopicResponse();
                fetchTopicResponse.deserialize(in);
                responses.add(fetchTopicResponse);
            }
        }
    }
}
