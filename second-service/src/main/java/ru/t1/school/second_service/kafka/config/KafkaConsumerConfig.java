package ru.t1.school.second_service.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import ru.t1.school.common.kafka.config.DefaultKafkaConsumerConfig;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.second_service.kafka.MessageDeserializer;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig extends DefaultKafkaConsumerConfig {

    private final KafkaProperties kafkaProps;

    @Bean("transactionAcceptListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionAcceptMessage> transactionAcceptListenerContainerFactory(
            @Qualifier("transactionAcceptConsumerFactory") ConsumerFactory<String, TransactionAcceptMessage> consumerFactory
    ) {
        return defaultKafkaListenerContainer(consumerFactory, log);
    }

    @Bean("transactionAcceptConsumerFactory")
    public ConsumerFactory<String, TransactionAcceptMessage> transactionAcceptConsumerFactory() {
        var factory = defaultConsumerListenerFactory(TransactionAcceptMessage.class, kafkaProps, MessageDeserializer.class);
        factory.updateConfigs(Map.of(
                ConsumerConfig.GROUP_ID_CONFIG,
                kafkaProps.getConsumer().getGroupId().getTransactionAccept()
        ));
        return factory;
    }
}

