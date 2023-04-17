package com.food.ordering.system.restaurant.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.restaurant.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.domain.event.OrderRejectedEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

import static com.food.ordering.system.domain.DomainConstants.UTC_ZONE;


@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

  @Override
  public OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
      DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
      DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {

    UUID orderId = restaurant.getOrderDetail().getId().getValue();

    restaurant.validateOrder(failureMessages);
    log.info("Validating order with id '{}'", orderId);

    if (failureMessages.isEmpty()) {
      log.info("Order is approved for order with id '{}'", orderId);
      restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);

      return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(),
          failureMessages, ZonedDateTime.now(UTC_ZONE), orderApprovedEventDomainEventPublisher);
    }

    log.info("Order is rejected for order with id '{}'", orderId);
    restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);

    return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(),
        failureMessages, ZonedDateTime.now(UTC_ZONE), orderRejectedEventDomainEventPublisher);
  }
}
