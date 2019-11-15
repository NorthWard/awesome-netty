package com.north.netty.kafka;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.north.netty.kafka.bean.*;
import com.north.netty.kafka.bean.fetch.FetchRequest;
import com.north.netty.kafka.bean.fetch.FetchResponse;
import com.north.netty.kafka.bean.fetch.FetchTopicPartitionRequest;
import com.north.netty.kafka.bean.fetch.FetchTopicRequest;
import com.north.netty.kafka.bean.meta.KafkaMetaRequest;
import com.north.netty.kafka.bean.meta.KafkaMetaResponse;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordBatch;
import com.north.netty.kafka.bean.msg.KafkaMsgRecordV2;
import com.north.netty.kafka.bean.produce.*;
import com.north.netty.kafka.caches.RequestCacheCenter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaProducer {
    private Channel channel;
    private String clientId;
    private AtomicInteger requestId = new AtomicInteger(1);

    public KafkaProducer(){
        this.clientId = "producer-1";
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
                        .addLast(new ByteToMessageDecoder() {
                            @Override
                            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                KafkaResponseHeader kafkaResponseHeader = new KafkaResponseHeader();
                                kafkaResponseHeader.deserialize(in);
                                if(kafkaResponseHeader.getCorrelationId() == null){
                                    throw new IllegalStateException("服务端返回的correlationId 为null");
                                }
                                AbstractKafkaResponse abstractKafkaResponse = RequestCacheCenter.getKafkaResponse(kafkaResponseHeader.getCorrelationId());
                                if(abstractKafkaResponse == null){
                                    throw new IllegalStateException("服务端返回的correlationId不是本客户端发送的");
                                }
                                abstractKafkaResponse.deserialize(in);
                                abstractKafkaResponse.setKafkaResponseHeader(kafkaResponseHeader);
                                abstractKafkaResponse.setCorrelationId(kafkaResponseHeader.getCorrelationId());
                                RequestCacheCenter.putKafkaResponse(kafkaResponseHeader.getCorrelationId(), abstractKafkaResponse);
                            }
                        });
            }
        });
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect("localhost", 9092).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void fetchMataData(){
        Integer xId = requestId.getAndIncrement();
        KafkaMetaRequest kafkaMetaRequest = new KafkaMetaRequest(clientId, xId);
        kafkaMetaRequest.setTopics(Lists.newArrayList("test"));
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        kafkaMetaRequest.serializable(byteBuf);
        try {
            RequestCacheCenter.putKafkaResponse(xId,  new KafkaMetaResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            AbstractKafkaResponse response =  RequestCacheCenter.waitForResp(xId, 400000);
            System.out.println(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void send(String key, String val){
        KafkaMsgRecordV2 kafkaMsgRecordV2 = null;
        try {
            kafkaMsgRecordV2 = new KafkaMsgRecordV2(key.getBytes("UTF8"), val.getBytes("UTF8"), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        KafkaMsgRecordBatch kafkaMsgRecordBatch = new KafkaMsgRecordBatch(kafkaMsgRecordV2);


        Record record = new Record();
        record.setPartition(0);
        record.setKafkaMsgRecordBatchList(new ArrayList<>());
        record.getKafkaMsgRecordBatchList().add(kafkaMsgRecordBatch);

        PartitionData partitionData = new PartitionData();
        partitionData.setRecordSset(record);

        TopicProduceData topicProduceData = new TopicProduceData();
        topicProduceData.setTopic("test");
        topicProduceData.setData(Lists.newArrayList(partitionData));

        Integer xid = requestId.getAndIncrement();
        ProduceRequest produceRequest = new ProduceRequest(clientId, xid);
        produceRequest.setAcks((short)-1);
        produceRequest.setTimeOut(30000);
        produceRequest.setTransactionalId(null);
        produceRequest.setTopicData(Lists.newArrayList(topicProduceData));


        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        produceRequest.serializable(byteBuf);


        try {
            RequestCacheCenter.putKafkaResponse(xid,  new ProduceResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            AbstractKafkaResponse response =  RequestCacheCenter.waitForResp(xid, 400000);
            System.out.println(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void poll(){
        Integer xid = requestId.getAndIncrement();
        FetchRequest fetchRequest = new FetchRequest(this.clientId, xid);

        FetchTopicPartitionRequest fetchTopicPartitionRequest = new FetchTopicPartitionRequest();

        fetchTopicPartitionRequest.setPartition(0);
        fetchTopicPartitionRequest.setFetchOffset(36L);
        fetchTopicPartitionRequest.setLogStartOffset(0L);
        fetchTopicPartitionRequest.setMaxBytes(Integer.MAX_VALUE);

        FetchTopicRequest fetchTopicRequest = new FetchTopicRequest();
        fetchTopicRequest.setTopic("test");
        fetchTopicRequest.setPartitions(new ArrayList<>());
        fetchTopicRequest.getPartitions().add(fetchTopicPartitionRequest);

        fetchRequest.setReplicaId(-1);
        fetchRequest.setMaxBytes(Integer.MAX_VALUE);
        fetchRequest.setMaxWaitTime(Integer.MAX_VALUE);
        fetchRequest.setMinBytes(0);
        byte b = 0;
        fetchRequest.setIsolationLevel(b);
        fetchRequest.setTopics(new ArrayList<>());
        fetchRequest.getTopics().add(fetchTopicRequest);


        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        fetchRequest.serializable(byteBuf);


        try {
            RequestCacheCenter.putKafkaResponse(xid,  new FetchResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            AbstractKafkaResponse response =  RequestCacheCenter.waitForResp(xid, 400000);
            System.out.println(new Gson().toJson(response));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
