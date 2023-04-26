package com.food.ordering.system.restaurant.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.OFFSET;
import static org.springframework.kafka.support.KafkaHeaders.PARTITION;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestKafkaListener implements
    KafkaConsumer<RestaurantApprovalRequestAvroModel> {

  private final RestaurantMessagingDataMapper mapper;
  private final RestaurantApprovalRequestMessageListener listener;

  @Override
  @KafkaListener(
      id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
      topics = "${restaurant-service.restaurant-approval-request-topic-name}")
  public void receive(
      @Payload List<RestaurantApprovalRequestAvroModel> messages,
      @Header(RECEIVED_KEY) List<String> keys,
      @Header(PARTITION) List<Integer> partitions,
      @Header(OFFSET) List<Long> offsets) {

    log.info(
        "'{}' number of order approval requests received with keys '{}, partitions '{}' " +
            "and offset '{}'", messages.size(), keys.toString(), partitions.toString(),
        offsets.toString());

    messages.forEach(message -> {
      log.info("Processing order approval event for order id '{}'", message.getOrderId());

      listener.approveOrder(
          mapper.restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(message));
    });
  }
}