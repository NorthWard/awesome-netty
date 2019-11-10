package test;

import com.north.netty.kafka.KafkaProducer;
import org.junit.Test;

public class kafkaProduceTest {
    @Test
    public void testMetaData() throws InterruptedException {
        KafkaProducer kafkaProducer = new KafkaProducer();
       // kafkaProducer.fetchMataData();
        kafkaProducer.send("099","000000000000");
    }
}
