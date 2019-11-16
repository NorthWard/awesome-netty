package test;

import com.google.gson.Gson;
import com.north.netty.kafka.KafkaClient;
import com.north.netty.kafka.bean.fetch.FetchPartitionHeader;
import com.north.netty.kafka.bean.fetch.FetchPartitionResp;
import com.north.netty.kafka.bean.fetch.FetchResponse;
import com.north.netty.kafka.bean.fetch.FetchTopicResponse;
import com.north.netty.kafka.bean.meta.KafkaMetaResponse;
import com.north.netty.kafka.bean.msg.ConsumerRecord;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordBatch;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordV2;
import com.north.netty.kafka.bean.produce.ProduceResponse;
import com.north.netty.kafka.bean.produce.Record;
import com.north.netty.kafka.config.KafkaConsumerConfig;
import com.north.netty.kafka.config.KafkaProduceConfig;
import com.north.netty.kafka.enums.Errors;
import com.north.netty.kafka.utils.StringSerializer;
import org.junit.Test;

import java.util.*;

public class kafkaClientTest {
    private final static String host = "localhost";
    private final static int port = 9092;
    private final static String topic = "testTopic123";

    @Test
    public void testMetaData(){
        KafkaClient kafkaClient = new KafkaClient("fetchMata-client", host, port);
        KafkaMetaResponse response = kafkaClient.fetchMataData(topic);
        assert  response != null;
        System.out.println(new Gson().toJson(response));
    }

    @Test
    public void testProducer(){
        KafkaClient kafkaClient = new KafkaClient("producer-111", host, port);
        KafkaProduceConfig kafkaConfig = new KafkaProduceConfig();
        // 注意这里设置为0时, broker不会响应任何数据, 但是消息实际上是发送到broker了的
        short ack = -1;
        kafkaConfig.setAck(ack);
        kafkaConfig.setTimeout(30000);
        ProduceResponse response  = kafkaClient.send(kafkaConfig, topic,"testKey","helloWorld1113");
        assert ack == 0 || response != null;
        System.out.println(new Gson().toJson(response));
    }

    @Test
    public void testConsumer(){
        // 如果broker上不存在这个topic的话, 直接消费可能会报错, 可以fetch一下metadata, 或先生产消息
        // testMetaData();
        // testProducer();
        KafkaClient kafkaClient = new KafkaClient("consumer-111", host, port);
        KafkaConsumerConfig consumerConfig = new KafkaConsumerConfig();
        consumerConfig.setMaxBytes(Integer.MAX_VALUE);
        consumerConfig.setMaxWaitTime(30000);
        consumerConfig.setMinBytes(1);
        Map<Integer, List<ConsumerRecord>>  response = kafkaClient.poll(consumerConfig, topic, 0, 0L);
        assert response != null && response.size() > 0;
        Set<Map.Entry<Integer, List<ConsumerRecord>>> entrySet =response.entrySet();
        for(Map.Entry<Integer, List<ConsumerRecord>> entry : entrySet){
            Integer partition = entry.getKey();
            System.out.println("partition" + partition + "的数据:");
            for(ConsumerRecord consumerRecord : entry.getValue()){
                System.out.println(new Gson().toJson(consumerRecord));
            }
        }

    }
}
