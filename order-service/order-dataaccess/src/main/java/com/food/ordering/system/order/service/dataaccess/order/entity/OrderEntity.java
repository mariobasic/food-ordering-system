package com.food.ordering.system.order.service.dataaccess.order.entity;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

  @Id
  private UUID id;
  private UUID customerId;
  private UUID restaurantId;
  private UUID trackingId;
  private BigDecimal price;
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
  private String failureMessages;

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private OrderAddressEntity address;

  // TODO: 20.3.23. test with MapsId¡
  // generate the ID for OrderAddressEntity
//  @MapsId
//  @OneToOne(cascade = CascadeType.ALL)
//  private OrderAddressEntity orderAddressEntity;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItemEntity> items = new ArrayList<>();


  public void addOrderAddressEntity(OrderAddressEntity orderAddress) {
    orderAddress.setOrder(this);
    this.address = orderAddress;
  }

  public void addOrderItemEntity(OrderItemEntity orderItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(orderItem);
    orderItem.setOrder(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderEntity that = (OrderEntity) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
