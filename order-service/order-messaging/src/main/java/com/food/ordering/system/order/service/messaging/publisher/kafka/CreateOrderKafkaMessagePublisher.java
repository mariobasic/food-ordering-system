package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publishers.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements
    OrderCreatedPaymentRequestMessagePublisher {

  private final OrderMessagingDataMapper mapper;
  private final OrderServiceConfigData configData;
  private final OrderKafkaMessageHelper orderKafkaMessageHelper;
  private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

  @Override
  public void publish(OrderCreatedEvent domainEvent) {
    String orderId = domainEvent.getOrder().getId().getValue().toString();
    log.info("Received OrderCreatedEvent for order id '{}'", orderId);


    try {
      var topicName = configData.paymentRequestTopicName();
      var topicMessage = mapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);

      kafkaProducer.send(topicName, orderId, topicMessage,
          orderKafkaMessageHelper.getHandler(
              topicName, topicMessage, orderId, "PaymentRequestAvroModel"));

      log.info("PaymentRequestAvroModel sent to Kafka for order id '{}'", orderId);
    } catch (Exception e) {
      log.error("Error while sending PaymentRequestAvroModel message " +
          "to kafka with order id '{}', error '{}'", orderId, e.getMessage());
    }
  }
}
