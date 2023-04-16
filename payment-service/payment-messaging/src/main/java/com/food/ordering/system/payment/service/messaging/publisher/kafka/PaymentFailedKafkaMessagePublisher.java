package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.ports.output.repository.message.publishers.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedKafkaMessagePublisher implements PaymentFailedMessagePublisher {

  private final PaymentMessagingDataMapper mapper;
  private final PaymentServiceConfigData configData;
  private final KafkaMessageHelper kafkaMessageHelper;
  private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

  @Override
  public void publish(PaymentFailedEvent domainEvent) {
    var orderId = domainEvent.getPayment().getOrderId().getValue().toString();
    log.info("Received PaymentFailedEvent for order id '{}'", orderId);

    try {
      var topicName = configData.paymentResponseTopicName();
      var message = mapper.paymentFailedEventToPaymentResponseAvroModel(domainEvent);

      kafkaProducer.send(
          topicName,
          orderId,
          message,
          kafkaMessageHelper.getHandler(topicName, message, orderId, "PaymentResponseAvroModel"));

      log.info("PaymentResponseAvroModel sent to Kafka for order id '{}'", orderId);
    } catch (Exception e) {
      log.error("Error while sending PaymentResponseAvroModel message " +
          "to kafka with order id '{}', error '{}'", orderId, e.getMessage());
    }
  }
}
