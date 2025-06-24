package ru.t1.school.the_best_starter.kafka.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.t1.school.common.kafka.config.DefaultKafkaProperties;

@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaProperties extends DefaultKafkaProperties {
}
