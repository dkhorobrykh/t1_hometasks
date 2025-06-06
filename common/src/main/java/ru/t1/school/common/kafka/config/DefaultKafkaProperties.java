package ru.t1.school.common.kafka.config;

import lombok.Data;

@Data
public abstract class DefaultKafkaProperties {
    private String bootstrapServers;
    private ProducerProperties producer;
    private ConsumerProperties consumer;
    private TopicProperties topic;

    @Data
    public static class ProducerProperties {
        private Integer retries;
        private Integer retryBackoffMs;
        private Boolean enableIdempotence;
        private Integer maxBlockMs;
        private Integer requestTimeoutMs;
        private Integer deliveryTimeoutMs;
    }

    @Data
    public static class ConsumerProperties {
        private Boolean useTypeInfoHeader;
        private Integer sessionTimeoutMs;
        private Integer maxPartitionFetchBytes;
        private Integer maxPollRecords;
        private Integer maxPollIntervalMs;
        private Boolean enableAutoCommit;
        private String autoOffsetCommit;
        private Integer heartbeatIntervalMs;
        private GroupsProperties groupId;
    }

    @Data
    public static class TopicProperties {
        private String metrics;
        private String transactionAccept;
        private String transactionResult;
        private String transaction;
    }

    @Data
    public static class GroupsProperties {
        private String metric;
        private String transaction;
        private String transactionAccept;
        private String transactionResult;
    }
}

