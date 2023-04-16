package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
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
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

  private final PaymentMessagingDataMapper mapper;
  private final PaymentRequestMessageListener listener;


  @Override
  @KafkaListener(
      id = "${kafka-consumer-config.payment-approval-consumer-group-id}",
      topics = "${payment-service.payment-request-topic-name}"
  )
  public void receive(
      @Payload List<PaymentRequestAvroModel> messages,
      @Header(RECEIVED_KEY) List<String> keys,
      @Header(PARTITION) List<Integer> partitions,
      @Header(OFFSET) List<Long> offsets) {

    log.info("'{}' number of payment requests received with keys '{}, partitions '{}' " +
            "and offset '{}'", messages.size(), keys.toString(), partitions.toString(),
        offsets.toString());

    messages.forEach(message -> {
      switch (message.getPaymentOrderStatus()) {
        case PENDING -> {
          log.info("Processing payment for order with id '{}'", message.getOrderId());
          listener.completePayment(mapper.paymentRequestAvroModelToPaymentRequest(message));
        }
        case CANCELLED -> {
          log.info("Cancelling payment for order with id '{}'", message.getOrderId());
          listener.cancelPayment(mapper.paymentRequestAvroModelToPaymentRequest(message));
        }
      }
    });
  }
}
