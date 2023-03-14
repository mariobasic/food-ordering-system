package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderDataMapper {


  public Restaurant toRestaurantFrom(CreateOrderCommand command) {

    return Restaurant.builder()
        .restaurantId(new RestaurantId(command.getRestaurantId()))
        .products(
            command.getItems().stream()
                .map(
                    com.food.ordering.system.order.service.domain.dto.create.OrderItem::getProductId)
                .map(ProductId::new)
                .map(Product::new)
                .toList())
        .build();
  }

  public Order toOrderFrom(CreateOrderCommand command) {
    return Order.builder()
        .customerId(new CustomerId(command.getCustomerId()))
        .restaurantId(new RestaurantId(command.getRestaurantId()))
        .deliveryAddress(orderAddressToStreetAddress(command.getAddress()))
        .price(new Money(command.getPrice()))
        .items(orderItemsToOrderItemsEntities(command.getItems()))
        .build();
  }

  private List<OrderItem> orderItemsToOrderItemsEntities(
      List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {
    return items.stream()
        .map(orderItem ->
            OrderItem.builder()
                .product(new Product(new ProductId(orderItem.getProductId())))
                .price(new Money(orderItem.getPrice()))
                .quantity(orderItem.getQuantity())
                .subTotal(new Money(orderItem.getSubTotal()))
                .build())
        .toList();
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
    return new StreetAddress(
        UUID.randomUUID(),
        address.getStreet(),
        address.getPostalCode(),
        address.getCity()
    );
  }

  public CreateOrderResponse toCreatedOrderResponseFrom(Order order, String message) {
    return CreateOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .message(message)
        .build();
  }

  public TrackOrderResponse toTrackOrderResponseFrom(Order order) {
    return TrackOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .failureMessages(order.getFailureMessages())
        .build();
  }
}

