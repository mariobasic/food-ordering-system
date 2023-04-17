package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.domain.RestaurantDomainService;
import com.food.ordering.system.restaurant.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestHelper {

  private final RestaurantDomainService restaurantDomainService;
  private final RestaurantDataMapper mapper;
  private final RestaurantRepository restaurantRepository;
  private final OrderApprovalRepository orderApprovalRepository;
  private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
  private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

  @Transactional
  public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest request) {
    log.info("processing restaurant approval for order with id '{}'", request.orderId());

    List<String> failureMessages = new ArrayList<>();

    var restaurant = findRestaurant(request);
    var orderApprovalEvent = restaurantDomainService.validateOrder(restaurant,
        failureMessages, orderApprovedMessagePublisher, orderRejectedMessagePublisher);

    orderApprovalRepository.save(restaurant.getOrderApproval());

    return orderApprovalEvent;
  }

  private Restaurant findRestaurant(RestaurantApprovalRequest request) {

    var restaurant = mapper.restaurantApprovalRequestToRestaurant(request);
    var restaurantEntity = getRestaurantEntity(restaurant);

    restaurantEntity.setActive(restaurantEntity.isActive());
    restaurant.getOrderDetail().getProducts().forEach(
        product -> restaurantEntity.getOrderDetail().getProducts().forEach(confirmedProduct -> {
          if (confirmedProduct.getId().equals(product.getId())) {
            product.updateWithConfirmedNamePriceAndAvailability(confirmedProduct.getName(),
                confirmedProduct.getPrice(), confirmedProduct.isAvailable());
          }
        }));
    // this is done in mapper don't know why it's included
//    restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(request.orderId())));

    return restaurant;
  }

  private Restaurant getRestaurantEntity(Restaurant restaurant) {

    restaurantRepository.findRestaurantInformation(restaurant)
        .orElseThrow(() -> {
          log.error("Restaurant with id '{}' not found!", restaurant.getId().getValue());
          return new RestaurantNotFoundException(
              "Restaurant with id '" + restaurant.getId().getValue() + "' not found!");
        });

    return restaurantRepository.findRestaurantInformation(restaurant)
        .orElseThrow(() -> {
          log.error("Restaurant with id '{}' not found!", restaurant.getId().getValue());
          return new RestaurantNotFoundException(
              "Restaurant with id '" + restaurant.getId().getValue() + "' not found!");
        });
  }
}
