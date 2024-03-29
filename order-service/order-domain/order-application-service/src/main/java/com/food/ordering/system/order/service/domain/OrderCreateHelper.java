package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publishers.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateHelper {

  private final OrderDomainService orderDomainService;
  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderDataMapper orderDataMapper;
  private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPublisher;

  @Transactional
  public OrderCreatedEvent persistOrder(CreateOrderCommand command) {
    checkCustomer(command.customerId());
    var restaurant = checkRestaurant(command);
    var order = orderDataMapper.toOrderFrom(command);
    var orderCreatedEvent =
        orderDomainService.validateAndInitiateOrder(order, restaurant, orderCreatedPublisher);

    saveOrder(order);
    log.info("Order is created with id '{}'", orderCreatedEvent.getOrder().getId().getValue());

    return orderCreatedEvent;
  }


  /**
   * If we needed to check more things we should do it inside the Customer domain, but since we are
   * only checking if it exists it's fine.
   */
  private void checkCustomer(UUID customerId) {
    if (customerRepository.findCustomer(customerId).isEmpty()) {
      log.warn("Could not find customer with customer id: '{}'", customerId);
      throw new OrderDomainException("Could not find customer with with id: " + customerId);
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  private Order saveOrder(Order order) {
    return Optional.ofNullable(orderRepository.save(order))
        .orElseThrow(() -> {
          log.error("Could not save order!");
          return new OrderDomainException("Could not save order!");
        });
  }

  private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
    Restaurant restaurant = orderDataMapper.toRestaurantFrom(createOrderCommand);

    return restaurantRepository.findRestaurantInformation(restaurant)
        .orElseThrow(() -> {
          UUID restaurantId = createOrderCommand.restaurantId();
          log.warn("Could not find restaurant with restaurant id '{}'", restaurantId);

          return new OrderDomainException("Couldn't find restaurant with id: " + restaurantId);
        });
  }
}
