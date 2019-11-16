package com.north.netty.kafka.config;

/**
 * @author laihaohua
 */
public class KafkaProduceConfig {
    /**
     * 发送消息的确认模式, 默认就是-1(all)
      */
    private short ack = -1;

    /**
     * 超时时间, 默认30秒
     */
    private int timeout = 30000;

    public short getAck() {
        return ack;
    }

    public void setAck(short ack) {
        this.ack = ack;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
