package com.food.ordering.system.restaurant.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDetail extends BaseEntity<OrderId> {

  private OrderStatus orderStatus;
  private Money totalAmount;
  private final List<Product> products;

  @Builder
  public OrderDetail(OrderId orderId, OrderStatus orderStatus, Money totalAmount,
      List<Product> products) {
    this.setId(orderId);
    this.orderStatus = orderStatus;
    this.totalAmount = totalAmount;
    this.products = products;
  }

}
