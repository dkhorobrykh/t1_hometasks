package ru.t1.school.main_project.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.t1.school.common.kafka.config.DefaultKafkaProducerConfig;
import ru.t1.school.common.kafka.dto.TransactionAcceptMessage;
import ru.t1.school.common.kafka.dto.TransactionMessage;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig extends DefaultKafkaProducerConfig {

    private final KafkaProperties kafkaProps;

    @Bean("transactionProducerFactory")
    public ProducerFactory<String, TransactionMessage> transactionProducerFactory() {
        return defaultProducerFactory(kafkaProps);
    }

    @Bean("transactionKafkaTemplate")
    public KafkaTemplate<String, TransactionMessage> transactionKafkaTemplate() {
        KafkaTemplate<String, TransactionMessage> template = new KafkaTemplate<>(transactionProducerFactory());
        template.setDefaultTopic(kafkaProps.getTopic().getTransaction());
        return template;
    }

    @Bean("transactionAcceptProducerFactory")
    public ProducerFactory<String, TransactionAcceptMessage> transactionAcceptProducerFactory() {
        return defaultProducerFactory(kafkaProps);
    }

    @Bean("transactionAcceptKafkaTemplate")
    public KafkaTemplate<String, TransactionAcceptMessage> transactionAcceptKafkaTemplate() {
        KafkaTemplate<String, TransactionAcceptMessage> template = new KafkaTemplate<>(transactionAcceptProducerFactory());
        template.setDefaultTopic(kafkaProps.getTopic().getTransactionAccept());
        return template;
    }
}
