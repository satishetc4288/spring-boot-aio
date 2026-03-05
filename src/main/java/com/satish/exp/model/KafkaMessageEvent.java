package com.satish.exp.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a message event received from a Kafka topic,
 * capturing both the payload and its metadata.
 */
@Data
@Builder
public class KafkaMessageEvent {

    /** The Kafka topic the message was received from. */
    private String topic;

    /** The partition within the topic. */
    private int partition;

    /** The offset of the record within the partition. */
    private long offset;

    /** The record key (may be null). */
    private String key;

    /** The record value / message payload. */
    private String value;
}
