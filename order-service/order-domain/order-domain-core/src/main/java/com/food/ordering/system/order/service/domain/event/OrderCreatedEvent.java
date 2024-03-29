package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import java.time.ZonedDateTime;

public final class OrderCreatedEvent extends OrderEvent {

  private final DomainEventPublisher<OrderCreatedEvent>  publisher;
  public OrderCreatedEvent(Order order, ZonedDateTime createdAt,
      DomainEventPublisher<OrderCreatedEvent> publisher) {
    super(order, createdAt);
    this.publisher = publisher;
  }

  @Override
  public void fire() {
    publisher.publish(this);
  }
}
