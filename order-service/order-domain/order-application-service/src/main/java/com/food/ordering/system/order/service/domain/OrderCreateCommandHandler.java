package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publishers.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommandHandler {

  private final OrderDataMapper orderDataMapper;
  private final OrderCreateHelper orderCreateHelper;
  private final OrderCreatedPaymentRequestMessagePublisher publisher;


  public CreateOrderResponse createOrder(CreateOrderCommand command) {
    var orderCreatedEvent = orderCreateHelper.persistOrder(command);
    log.info("Order is created with id '{}'", orderCreatedEvent.getOrder().getId().getValue());

    publisher.publish(orderCreatedEvent);

    return orderDataMapper.toCreatedOrderResponseFrom(orderCreatedEvent.getOrder(),
        "Order created successfully");
  }

}
