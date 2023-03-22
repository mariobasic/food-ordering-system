package com.food.ordering.system.kafka.config.data;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kafka-config")
public record KafkaConfigData(
    String bootstrapServers,
    String schemaRegistryUrlKey,
    String schemaRegistryUrl,
    Integer numOfPartitions,
    Short replicationFactor) {

}
