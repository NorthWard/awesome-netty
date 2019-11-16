package com.north.netty.kafka;

import com.google.common.collect.Lists;
import com.north.netty.kafka.bean.fetch.*;
import com.north.netty.kafka.bean.meta.KafkaMetaRequest;
import com.north.netty.kafka.bean.meta.KafkaMetaResponse;
import com.north.netty.kafka.bean.msg.ConsumerRecord;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordBatch;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordV2;
import com.north.netty.kafka.bean.produce.*;
import com.north.netty.kafka.caches.RequestCacheCenter;
import com.north.netty.kafka.codec.KafkaResponseDecoder;
import com.north.netty.kafka.config.KafkaConsumerConfig;
import com.north.netty.kafka.config.KafkaProduceConfig;
import com.north.netty.kafka.enums.Errors;
import com.north.netty.kafka.utils.SimplePartitioner;
import com.north.netty.kafka.utils.StringSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laihaohua
 */
public class KafkaClient {
    private Channel channel;
    private String clientId;
    private RequestCacheCenter requestCacheCenter = new RequestCacheCenter();
    private AtomicInteger requestId = new AtomicInteger(1);

    public KafkaClient(String clientId, String  host, int port){
        this.clientId = clientId;
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LengthFieldPrepender(4))
                        .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
                        .addLast(new KafkaResponseDecoder(requestCacheCenter));
            }
        });
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(host, port).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public KafkaMetaResponse fetchMataData(String topic){
        Integer xId = requestId.getAndIncrement();
        KafkaMetaRequest kafkaMetaRequest = new KafkaMetaRequest(clientId, xId);
        kafkaMetaRequest.setTopics(Lists.newArrayList(topic));
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        kafkaMetaRequest.serializable(byteBuf);
        try {
            requestCacheCenter.putKafkaResponse(xId,  new KafkaMetaResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            KafkaMetaResponse response =  (KafkaMetaResponse)requestCacheCenter.waitForResp(xId, 400000);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ProduceResponse send(KafkaProduceConfig config, String topic , String key, String val){

        if(config == null || topic == null || topic.isEmpty() || val == null || val.isEmpty()){
            throw  new IllegalArgumentException("topic和val都不能为空");
        }

        // 序列化器  key 和 val都简单地用String序列化器
        byte [] keyBytes = StringSerializer.getBytes(key);
        byte [] valBytes = StringSerializer.getBytes(val);

        // 分区器
        int partition = SimplePartitioner.getPartion(topic, keyBytes, valBytes);

        KafkaMsgRecordV2 kafkaMsgRecordV2 = new KafkaMsgRecordV2(keyBytes, valBytes , null);
        KafkaMsgRecordBatch kafkaMsgRecordBatch = new KafkaMsgRecordBatch(kafkaMsgRecordV2);

        Record record = new Record();
        // 指定只发送到哪个分区
        record.setPartition(partition);
        record.setKafkaMsgRecordBatchList(new ArrayList<>());
        record.getKafkaMsgRecordBatchList().add(kafkaMsgRecordBatch);

        PartitionData partitionData = new PartitionData();
        partitionData.setRecordSset(record);

        TopicProduceData topicProduceData = new TopicProduceData();
        topicProduceData.setTopic(topic);
        topicProduceData.setData(Lists.newArrayList(partitionData));

        Integer xid = requestId.getAndIncrement();
        ProduceRequest produceRequest = new ProduceRequest(clientId, xid);
        produceRequest.setAcks(config.getAck());
        produceRequest.setTimeOut(config.getTimeout());
        produceRequest.setTransactionalId(null);
        produceRequest.setTopicData(Lists.newArrayList(topicProduceData));


        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        produceRequest.serializable(byteBuf);
        try {
            requestCacheCenter.putKafkaResponse(xid,  new ProduceResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            ProduceResponse response =  (ProduceResponse)requestCacheCenter.waitForResp(xid, config.getTimeout());
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }


    public Map<Integer, List<ConsumerRecord>> poll(KafkaConsumerConfig consumerConfig, String topic, int partition, long fetchOffset){
        if(consumerConfig == null || topic == null){
            throw  new IllegalArgumentException("必要参数不能为空");
        }

        Integer xid = requestId.getAndIncrement();
        FetchRequest fetchRequest = new FetchRequest(this.clientId, xid);

        FetchTopicPartitionRequest fetchTopicPartitionRequest = new FetchTopicPartitionRequest();

        fetchTopicPartitionRequest.setPartition(partition);
        fetchTopicPartitionRequest.setFetchOffset(fetchOffset);
        fetchTopicPartitionRequest.setLogStartOffset(0L);
        fetchTopicPartitionRequest.setMaxBytes(consumerConfig.getMaxBytes());

        FetchTopicRequest fetchTopicRequest = new FetchTopicRequest();
        fetchTopicRequest.setTopic(topic);
        fetchTopicRequest.setPartitions(new ArrayList<>());
        fetchTopicRequest.getPartitions().add(fetchTopicPartitionRequest);

        fetchRequest.setReplicaId(-1);
        fetchRequest.setMaxBytes(consumerConfig.getMaxBytes());
        fetchRequest.setMaxWaitTime(consumerConfig.getMaxWaitTime());
        fetchRequest.setMinBytes(consumerConfig.getMinBytes());
        byte b = 0;
        fetchRequest.setIsolationLevel(b);
        fetchRequest.setTopics(new ArrayList<>());
        fetchRequest.getTopics().add(fetchTopicRequest);


        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        fetchRequest.serializable(byteBuf);


        try {
            requestCacheCenter.putKafkaResponse(xid,  new FetchResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            FetchResponse response =  (FetchResponse)requestCacheCenter.waitForResp(xid, consumerConfig.getMaxWaitTime());
            return parseResp(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * kafka poll 返回的数据结构非常复杂, 这里解析出我们想要的数据
     * @param response
     * @return
     */
    private Map<Integer, List<ConsumerRecord>> parseResp(FetchResponse response ){
        if(response == null){
            return null;
        }
        Map<Integer, List<ConsumerRecord>> partitionRecordMap = new HashMap<>();
        for(FetchTopicResponse fetchTopicResponse : response.getResponses()){
            // 由于我们这里的实现只会订阅一个topic, 所以这里没有用到这个
            String topic = fetchTopicResponse.getTopic();
            List<FetchPartitionResp> partitionResps = fetchTopicResponse.getPartitionResps();
            assert  partitionResps != null;
            // 遍历这个topic的每一个分区
            for(FetchPartitionResp partitionResp : partitionResps){
                FetchPartitionHeader fetchPartitionHeader = partitionResp.getPartitionHeaders();
                short errorCode = fetchPartitionHeader.getErrorCode();
                if(errorCode != Errors.NONE.code()){
                    // 这个分区的响应有错误的话 抛出异常
                    throw new IllegalArgumentException("broker 返回错误: " + Errors.forCode(errorCode).message());
                }
                // 初始化这个分区的list
                Integer partition = fetchPartitionHeader.getPartition();
                if(!partitionRecordMap.containsKey(partition)){
                    partitionRecordMap.putIfAbsent(partition, new ArrayList<>());
                }
                Record record =  partitionResp.getRecordSset();
                assert  record != null;
                List<KafkaMsgRecordBatch>  kafkaMsgRecordBatchList = record.getKafkaMsgRecordBatchList();
                // kafka的响应可能会分成很多个批次, 所以这里要一个批次一个批次地处理
                for(KafkaMsgRecordBatch kafkaMsgRecordBatch : kafkaMsgRecordBatchList){
                    Long baseOffset = kafkaMsgRecordBatch.getBaseOffset();
                    long firstTimestamp = kafkaMsgRecordBatch.getFirstTimestamp();
                    List<KafkaMsgRecordV2>  kafkaMsgRecordV2List = kafkaMsgRecordBatch.getMsgs();
                    for(KafkaMsgRecordV2 kafkaMsgRecordV2 : kafkaMsgRecordV2List){
                        ConsumerRecord consumerRecord = new ConsumerRecord();
                        long offset = baseOffset + kafkaMsgRecordV2.getOffsetDelta();
                        consumerRecord.setOffset(offset);
                        long timestamp = firstTimestamp + kafkaMsgRecordV2.getTimestampDelta();
                        consumerRecord.setTimeStamp(timestamp);
                        consumerRecord.setKey(StringSerializer.getString(kafkaMsgRecordV2.getKey()));
                        consumerRecord.setVal(StringSerializer.getString(kafkaMsgRecordV2.getValues()));
                        partitionRecordMap.get(partition).add(consumerRecord);
                    }
                }

            }
        }
        return partitionRecordMap;
    }
}
