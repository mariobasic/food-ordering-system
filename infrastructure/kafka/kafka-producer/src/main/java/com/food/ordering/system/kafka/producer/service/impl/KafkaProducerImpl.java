package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements
    KafkaProducer<K, V> {

  private final KafkaTemplate<K, V> kafkaTemplate;

  // TODO: 16.3.23. see how to handle deprecation of listenable future
  @Override
//  public void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> result) {
  public void send(String topicName, K key, V message,
      BiConsumer<SendResult<K, V>, Throwable> handler) {
    log.info("Sending message '{}' to topic '{}'", message, topicName);

    try {
      CompletableFuture<SendResult<K, V>> send = kafkaTemplate.send(topicName, key, message);
      send.whenComplete(handler);
    } catch (KafkaException e) {
      log.error("Error on kafka producer with key '{}', message '{}' and exception message '{}'",
          key, message, e.getMessage());
      throw new KafkaProducerException(
          "Error on kafka producer with key '" + key + " and message " + message);
    }
  }



  public void close() {
    if (kafkaTemplate != null) {
      log.info("Closing kafka producer");
      kafkaTemplate.destroy();
    }
  }

}
