package com.food.ordering.system.restaurant.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.entity.OrderApproval;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

  private final DomainEventPublisher<OrderApprovedEvent> publisher;

  public OrderApprovedEvent(
      OrderApproval orderApproval,
      RestaurantId restaurantId,
      List<String> failureMessages,
      ZonedDateTime createdAt,
      DomainEventPublisher<OrderApprovedEvent> publisher) {
    super(restaurantId, orderApproval, createdAt, failureMessages);
    this.publisher = publisher;
  }

  @Override
  public void fire() {
    publisher.publish(this);
  }
}
