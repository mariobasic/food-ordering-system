package com.food.ordering.system.restaurant.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.domain.entity.Product;
import com.food.ordering.system.restaurant.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataMapper {

  public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest request) {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(UUID.fromString(request.restaurantId())))
        .orderDetail(
            OrderDetail.builder()
                .orderId(new OrderId(UUID.fromString(request.orderId())))
                .products(request.products().stream()
                    .map(product ->
                        Product.builder()
                            .productId(product.getId())
                            .quantity(product.getQuantity())
                            .build())
                    .toList())
                .totalAmount(new Money(request.price()))
                .orderStatus(OrderStatus.valueOf(request.restaurantOrderStatus().name()))
                .build())
        .build();
  }
}
