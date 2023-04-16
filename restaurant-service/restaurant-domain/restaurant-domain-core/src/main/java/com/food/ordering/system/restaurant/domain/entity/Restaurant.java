package com.food.ordering.system.restaurant.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.valueobject.OrderApprovalId;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

  private OrderApproval orderApproval;
  @Setter
  private boolean active;
  private final OrderDetail orderDetail;

  @Builder
  public Restaurant(RestaurantId restaurantId, OrderApproval orderApproval, boolean active,
      OrderDetail orderDetail) {
    this.setId(restaurantId);
    this.orderApproval = orderApproval;
    this.active = active;
    this.orderDetail = orderDetail;
  }

  public void validateOrder(List<String> failureMessages) {
    if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
      failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
    }

    Money totalAmount = orderDetail.getProducts().stream()
        .map(product -> {
          if (!product.isAvailable()) {
            failureMessages.add(
                "Product with id: " + product.getId().getValue() + " is not available");
          }

          return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO, Money::add);

    if (!totalAmount.equals(orderDetail.getTotalAmount())) {
      failureMessages.add("Price order is not correct for order: " + orderDetail.getId());
    }
  }

  public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
    this.orderApproval = OrderApproval.builder()
        .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
        .restaurantId(this.getId())
        .orderId(this.orderDetail.getId())
        .approvalStatus(orderApprovalStatus)
        .build();
  }

}
