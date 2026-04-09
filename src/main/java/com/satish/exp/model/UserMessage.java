package com.satish.exp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Example POJO to demonstrate deserialization of Kafka message values.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {
    private String id;
    private String content;
    private long timestamp;
}
