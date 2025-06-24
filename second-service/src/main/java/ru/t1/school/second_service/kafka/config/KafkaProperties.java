package ru.t1.school.second_service.kafka.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.t1.school.common.kafka.config.DefaultKafkaProperties;

@EqualsAndHashCode(callSuper = true)
@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties extends DefaultKafkaProperties {
}
