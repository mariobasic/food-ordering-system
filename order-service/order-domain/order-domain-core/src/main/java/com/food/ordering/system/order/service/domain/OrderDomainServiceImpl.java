package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

import static com.food.ordering.system.domain.DomainConstants.UTC_ZONE;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {


  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant,
      DomainEventPublisher<OrderCreatedEvent> publisher) {
    validateRestaurant(restaurant);
    setOrderProductInformation(order, restaurant);
    order.validateOrder();
    order.initializeOrder();

    log.info("Order with id: '{}' is initiated", order.getId().getValue());

    return new OrderCreatedEvent(order, ZonedDateTime.now(UTC_ZONE), publisher);
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
          " is currently not active!");
    }
  }

  private void setOrderProductInformation(Order order, Restaurant restaurant) {

    Map<ProductId, Product> restaurantProductPerId = restaurant.getProducts().stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));

    order.getItems().stream()
        .map(OrderItem::getProduct)
        .forEach(orderProduct ->
            Optional.ofNullable(restaurantProductPerId.get(orderProduct.getId()))
                .ifPresent(restaurantProduct ->

                    orderProduct.updateWithConfirmedNameAndPrice(
                        restaurantProduct.getName(),
                        restaurantProduct.getPrice())));

  }

  @Override
  public OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> publisher) {
    order.pay();
    log.info("Order with id '{}' is paid", order.getId().getValue());

    return new OrderPaidEvent(order, ZonedDateTime.now(UTC_ZONE), publisher);
  }

  @Override
  public void approveOrder(Order order) {
    order.approve();
    log.info("Order with id '{}' is approved", order.getId().getValue());
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,
      DomainEventPublisher<OrderCancelledEvent> publisher) {
    order.initCancel(failureMessages);
    log.info("Order payment is cancelling for order id '{}'", order.getId().getValue());

    return new OrderCancelledEvent(order, ZonedDateTime.now(UTC_ZONE), publisher);
  }

  @Override
  public void cancelOrder(Order order, List<String> failureMessages) {
    order.cancel(failureMessages);
    log.info("Order with id '{}' is cancelled", order.getId().getValue());
  }
}
