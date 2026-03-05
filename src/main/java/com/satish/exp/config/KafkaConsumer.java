package com.satish.exp.config;

import com.satish.exp.model.KafkaMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer that listens on the configured topic and logs
 * each received message together with its metadata.
 *
 * <p>
 * The topic and group-id are resolved from {@code application.properties}:
 * 
 * <pre>
 *   spring.kafka.consumer.topic=satish-aio-topic
 *   spring.kafka.consumer.group-id=satish-aio-consumer-group
 * </pre>
 */
@Slf4j
@Component
public class KafkaConsumer {

    /**
     * Processes a message from the Kafka topic.
     *
     * @param record the full {@link ConsumerRecord} carrying topic, partition,
     *               offset, key, and value
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) {
        KafkaMessageEvent event = KafkaMessageEvent.builder()
                .topic(record.topic())
                .partition(record.partition())
                .offset(record.offset())
                .key(record.key())
                .value(record.value())
                .build();

        log.info("[KafkaConsumer] Received | topic={} partition={} offset={} key={} value={}",
                event.getTopic(),
                event.getPartition(),
                event.getOffset(),
                event.getKey(),
                event.getValue());
    }
}
