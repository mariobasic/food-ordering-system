package com.food.ordering.system.restaurant.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {

  private String name;
  private Money price;
  private final int quantity;
  private boolean available;

  @Builder
  public Product(ProductId productId, String name, Money price, int quantity, boolean available) {
    this.setId(productId);
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.available = available;
  }

  public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
    this.name = name;
    this.price = price;
    this.available = available;
  }
}
