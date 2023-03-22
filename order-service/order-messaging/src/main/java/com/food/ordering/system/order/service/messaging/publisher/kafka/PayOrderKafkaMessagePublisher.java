package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publishers.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

  private final OrderMessagingDataMapper mapper;
  private final OrderServiceConfigData configData;
  private final OrderKafkaMessageHelper orderKafkaMessageHelper;
  private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

  @Override
  public void publish(OrderPaidEvent domainEvent) {
    String orderId = domainEvent.getOrder().getId().getValue().toString();
    log.info("Received OrderPaidEvent for order id '{}'", orderId);

    try {
      var topicName = configData.restaurantApprovalRequestTopicName();
      var topicMessage = mapper.orderPaidEventToRestaurantApprovalRequestAvroModel(
          domainEvent);

      kafkaProducer.send(topicName, orderId, topicMessage,
          orderKafkaMessageHelper.getHandler
              (topicName, topicMessage, orderId, "RestaurantApprovalRequestAvroModel"));

      log.info("RestaurantApprovalRequestAvroModel sent to kafka for order id '{}'", orderId);
    } catch (Exception e) {
      log.error("Error while sending RestaurantApprovalRequestAvroModel message " +
          "to kafka with order id '{}, error: ", orderId, e);
    }
  }
}
