package ru.t1.school.the_best_starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import ru.t1.school.the_best_starter.kafka.config.KafkaProperties;

@Data
@ConfigurationProperties(prefix = "the-best-starter")
public class StarterProperties {
    @NestedConfigurationProperty
    private KafkaProperties kafka;
}
