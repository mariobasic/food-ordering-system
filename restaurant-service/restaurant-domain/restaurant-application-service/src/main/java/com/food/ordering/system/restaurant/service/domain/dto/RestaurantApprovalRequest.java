package com.food.ordering.system.restaurant.service.domain.dto;

import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.restaurant.domain.entity.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record RestaurantApprovalRequest(
    String id,
    String sagaId,
    String restaurantId,
    String orderId,
    RestaurantOrderStatus restaurantOrderStatus,
    List<Product> products,
    BigDecimal price,
    Instant createdAt
) {

}
