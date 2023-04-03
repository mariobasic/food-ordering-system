package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publishers.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderKafkaMessagePublisher implements
    OrderCancelledPaymentRequestMessagePublisher {

  private final OrderMessagingDataMapper mapper;
  private final OrderServiceConfigData configData;

  private final OrderKafkaMessageHelper orderKafkaMessageHelper;
  private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;


  @Override
  public void publish(OrderCancelledEvent domainEvent) {
    String orderId = domainEvent.getOrder().getId().getValue().toString();
    log.info("Received OrderCancelledEvent for order id '{}'", orderId);

    try {
      var topicName = configData.paymentRequestTopicName();
      var message = mapper.orderCanceledEventToPaymentRequestAvroModel(domainEvent);

      kafkaProducer.send(topicName, orderId, message,
          orderKafkaMessageHelper.getHandler(
              topicName, message, orderId, "PaymentRequestAvroModel"));

      log.info("PaymentRequestAvroModel sent to Kafka for order id '{}'", orderId);
    } catch (Exception e) {
      log.error("Error while sending PaymentRequestAvroModel message " +
          "to kafka with order id '{}', error '{}'", orderId, e.getMessage());
    }
  }


}
