package ru.t1.school.common.kafka.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class DefaultKafkaProperties {
    private String bootstrapServers;
    @NestedConfigurationProperty
    private ProducerProperties producer = new ProducerProperties();
    @NestedConfigurationProperty
    private ConsumerProperties consumer = new ConsumerProperties();
    @NestedConfigurationProperty
    private TopicProperties topic = new TopicProperties();

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
        @NestedConfigurationProperty
        private GroupsProperties groupId = new GroupsProperties();
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

