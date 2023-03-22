package com.food.ordering.system.order.service.messaging.publisher.kafka;


import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

  // TODO: 21.3.23. default method in parent interface?

  public <T> BiConsumer<SendResult<String, T>, Throwable> getHandler(
      String topicName, T message, String orderId, String modelName) {

    return (result, throwable) -> {
      if (throwable != null) {
        log.error("Error while sending '{}' message '{}' to topic '{}'",
           modelName, message.toString(), topicName, throwable);

      } else {
        RecordMetadata metadata = result.getRecordMetadata();
        log.error("Received successful response from Kafka for order id '{}" +
                " topic '{}', partition '{}', offset '{}', timestamp '{}'",
            orderId,
            metadata.topic(),
            metadata.partition(),
            metadata.offset(),
            metadata.timestamp());
      }
    };
  }

}
