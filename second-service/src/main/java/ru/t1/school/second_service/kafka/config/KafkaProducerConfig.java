package ru.t1.school.second_service.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.t1.school.common.kafka.config.DefaultKafkaProducerConfig;
import ru.t1.school.common.kafka.dto.TransactionResultMessage;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
public class KafkaProducerConfig extends DefaultKafkaProducerConfig {

    private final KafkaProperties kafkaProps;

    @Bean("transactionResultProducerFactory")
    public ProducerFactory<String, TransactionResultMessage> transactionProducerFactory() {
        return defaultProducerFactory(kafkaProps);
    }

    @Bean("transactionResultKafkaTemplate")
    public KafkaTemplate<String, TransactionResultMessage> transactionResultKafkaTemplate() {
        KafkaTemplate<String, TransactionResultMessage> template = new KafkaTemplate<>(transactionProducerFactory());
        template.setDefaultTopic(kafkaProps.getTopic().getTransactionResult());
        return template;
    }


}
