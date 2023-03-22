package com.food.ordering.system.order.service.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.mapper.OrderMessagingDataMapper;
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
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

  private final OrderMessagingDataMapper mapper;
  private final PaymentResponseMessageListener listener;

  @Override
  @KafkaListener(
      id = "${kafka-consumer-config.payment-consumer-group-id}",
      topics = "${order-service.payment-response-topic-name}"
  )
  public void receive(
      @Payload List<PaymentResponseAvroModel> messages,
      @Header(RECEIVED_KEY) List<String> keys,
      @Header(PARTITION) List<Integer> partitions,
      @Header(OFFSET) List<Long> offsets) {

    log.info("'{}' number of payment responses received with keys '{}, partitions '{}' " +
            "and offset '{}'", messages.size(), keys.toString(), partitions.toString(),
        offsets.toString());

    messages.forEach(message -> {
      switch (message.getPaymentStatus()) {
        case COMPLETED -> {
          log.info("Processing successful payment for order id '{}'", message.getOrderId());
          listener.paymentCompleted(mapper.paymentResponseAvroModelToPaymentResponse(message));
        }
        case CANCELLED, FAILED -> {
          log.info("Processing unsuccessful payment for order id '{}'", message.getOrderId());
          listener.paymentCanceled(mapper.paymentResponseAvroModelToPaymentResponse(message));
        }
      }
    });
  }
}
