package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

import static com.food.ordering.system.domain.DomainConstants.UTC_ZONE;

@Getter
public class Payment extends AggregateRoot<PaymentId> {

  private final OrderId orderId;
  private final CustomerId customerId;
  private final Money price;

  private PaymentStatus paymentStatus;
  private ZonedDateTime createdAt;

  public void initializePayment() {
    setId(new PaymentId(UUID.randomUUID()));
    createdAt = ZonedDateTime.now(UTC_ZONE);
  }

  public void validatePayment(List<String> failureMessages) {
    if (price == null || !price.isGreaterThanZero()) {
      failureMessages.add("Total price must be greater than zero!");
    }
  }

  public void updateStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  @Builder
  public Payment(PaymentId paymentId, OrderId orderId, CustomerId customerId, Money price,
      PaymentStatus paymentStatus, ZonedDateTime createdAt) {
    this.setId(paymentId);
    this.orderId = orderId;
    this.customerId = customerId;
    this.price = price;
    this.paymentStatus = paymentStatus;
    this.createdAt = createdAt;
  }
}
