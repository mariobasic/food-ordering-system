package com.food.ordering.system.order.service.dataaccess.order.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntityId implements Serializable {

  // TODO: 20.3.23. test with embedded and embeddable
  private Long id;
  private OrderEntity order;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemEntityId that = (OrderItemEntityId) o;
    return id.equals(that.id) && order.equals(that.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, order);
  }
}
