package me.trayl.redirect;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class KafkaLogService {

    private final static String TOPIC = "redirect-log";

    private final Logger logger = LogManager.getLogger();

    private final KafkaProducer<Long, Long> producer;

    public KafkaLogService() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/java.config")) {
           props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer");
        this.producer = new KafkaProducer<>(props);
    }


    public void logAccess(long id) {
        producer.send(new ProducerRecord<>(TOPIC, id, id), (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
        });
    }
}
