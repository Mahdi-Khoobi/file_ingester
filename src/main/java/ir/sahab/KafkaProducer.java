package ir.sahab;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducer {
    public static boolean produceLog(LogModel logModel) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        Producer<String, LogModel> kafkaProducer =
                new org.apache.kafka.clients.producer.KafkaProducer<>(
                        props,
                        new StringSerializer(),
                        new KafkaJsonSerializer()
                );
        // Send a message
        kafkaProducer.send(new ProducerRecord<>("TOPIC", null, logModel));
        kafkaProducer.flush();
        //kafkaProducer.close();
        System.out.println("Hello world!");
        return true;
    }
}