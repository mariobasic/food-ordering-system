package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderDataAccessMapper {

  public OrderEntity orderToOrderEntity(Order order) {
    OrderEntity orderEntity = OrderEntity.builder()
        .id(order.getId().getValue())
        .customerId(order.getCustomerId().getValue())
        .restaurantId(order.getRestaurantId().getValue())
        .trackingId(order.getTrackingId().getValue())
//        .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
        .price(order.getPrice().amount())
//        .items(orderItemsToOrderItemsEntity(order.getItems()))
        .orderStatus(order.getOrderStatus())
        .failureMessages(order.getFailureMessages() != null ?
            String.join(",", order.getFailureMessages()) : "")
        .build();

    orderEntity.addOrderAddressEntity(deliveryAddressToAddressEntity(order.getDeliveryAddress()));
    orderItemsToOrderItemsEntity(order.getItems()).forEach(orderEntity::addOrderItemEntity);

    return orderEntity;
  }

  private List<OrderItemEntity> orderItemsToOrderItemsEntity(List<OrderItem> items) {
    return items.stream()
        .map(item ->
            OrderItemEntity.builder()
                .id(item.getId().getValue())
                .productId(item.getProduct().getId().getValue())
                .price(item.getPrice().amount())
                .quantity(item.getQuantity())
                .subTotal(item.getSubTotal().amount())
                .build())
        .toList();
  }

  private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
    return OrderAddressEntity.builder()
        .id(deliveryAddress.id())
        .street(deliveryAddress.street())
        .postalCode(deliveryAddress.postalCode())
        .city(deliveryAddress.city())
        .build();
  }

  public Order orderEntityToOrder(OrderEntity entity) {
    return Order.builder()
        .orderId(new OrderId(entity.getId()))
        .customerId(new CustomerId(entity.getCustomerId()))
        .restaurantId(new RestaurantId(entity.getRestaurantId()))
        .deliveryAddress(addressEntityToDeliveryAddress(entity.getAddress()))
        .price(new Money(entity.getPrice()))
        .items(orderItemEntitiesToOrderItems(entity.getItems()))
        .trackingId(new TrackingId(entity.getTrackingId()))
        .orderStatus(entity.getOrderStatus())
        .failureMessages(entity.getFailureMessages().isEmpty() ?
            new ArrayList<>() :
            new ArrayList<>(Arrays.asList(entity.getFailureMessages().split(","))))
        .build();

  }

  private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
    return items.stream()
        .map(item ->
            OrderItem.builder()
                .orderItemId(new OrderItemId(item.getId()))
                .product(new Product(new ProductId(item.getProductId())))
                .price(new Money(item.getPrice()))
                .quantity(item.getQuantity())
                .subTotal(new Money(item.getSubTotal()))
                .build())
        .toList();
  }

  private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
    return new StreetAddress(address.getId(), address.getStreet(), address.getPostalCode(),
        address.getCity());
  }

}
