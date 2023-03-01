package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

  private final List<Product> products;
  private final boolean active;

  @Builder
  public Restaurant(RestaurantId restaurantId, List<Product> products, boolean active) {
    this.setId(restaurantId);
    this.products = products;
    this.active = active;
  }
}
