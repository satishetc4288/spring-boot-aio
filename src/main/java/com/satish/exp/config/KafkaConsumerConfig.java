package com.satish.exp.config;

import com.satish.exp.model.UserMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static com.satish.exp.commons.ApplicationConstants.KAFKA;

/**
 * Kafka consumer configuration.
 * <p>
 * {@code @EnableKafka} activates the {@code @KafkaListener} annotation
 * processing infrastructure, which is required when defining a custom listener
 * container factory.
 * <p>
 * Uses {@link JacksonJsonDeserializer} (Jackson 3 compatible, Spring Kafka 4.x+)
 * in place of the removed {@code JsonDeserializer}.
 */
@EnableKafka
@Configuration
@Profile(KAFKA)
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Creates a {@link ConsumerFactory} for {@code String} key and POJO value records.
     */
    @Bean
    public ConsumerFactory<String, UserMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, UserMessage.class.getName());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Creates the {@link ConcurrentKafkaListenerContainerFactory} that wires
     * the consumer factory into Spring's {@code @KafkaListener} infrastructure.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
