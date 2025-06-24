package ru.t1.school.main_project.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import ru.t1.school.common.kafka.config.DefaultKafkaConsumerConfig;
import ru.t1.school.common.kafka.dto.TransactionMessage;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;
import ru.t1.school.main_project.kafka.MessageDeserializer;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig extends DefaultKafkaConsumerConfig {

    private final KafkaProperties kafkaProps;

    @Bean("transactionListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionMessage> transactionListenerContainerFactory(
            @Qualifier("transactionConsumerFactory") ConsumerFactory<String, TransactionMessage> consumerFactory
    ) {
        return defaultKafkaListenerContainer(consumerFactory, log);
    }

    @Bean("transactionConsumerFactory")
    public ConsumerFactory<String, TransactionMessage> transactionConsumerFactory() {
        var factory = defaultConsumerListenerFactory(TransactionMessage.class, kafkaProps, MessageDeserializer.class);
        factory.updateConfigs(Map.of(
                ConsumerConfig.GROUP_ID_CONFIG,
                kafkaProps.getConsumer().getGroupId().getTransaction()
        ));
        return factory;
    }

    @Bean("transactionResultListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionResultMessage> transactionResultListenerContainerFactory(
            @Qualifier("transactionResultConsumerFactory") ConsumerFactory<String, TransactionResultMessage> consumerFactory
    ) {
        return defaultKafkaListenerContainer(consumerFactory, log);
    }

    @Bean("transactionResultConsumerFactory")
    public ConsumerFactory<String, TransactionResultMessage> transactionResultConsumerFactory() {
        var factory = defaultConsumerListenerFactory(TransactionResultMessage.class, kafkaProps, MessageDeserializer.class);
        factory.updateConfigs(Map.of(
                ConsumerConfig.GROUP_ID_CONFIG,
                kafkaProps.getConsumer().getGroupId().getTransactionResult()
        ));
        return factory;
    }
}
