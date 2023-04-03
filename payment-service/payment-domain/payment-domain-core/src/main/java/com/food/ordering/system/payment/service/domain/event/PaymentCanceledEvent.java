package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PaymentCanceledEvent extends PaymentEvent {

  private final DomainEventPublisher<PaymentCanceledEvent> publisher;

  public PaymentCanceledEvent(Payment payment,
      ZonedDateTime createdAt, DomainEventPublisher<PaymentCanceledEvent> publisher) {
    super(payment, createdAt, new ArrayList<>());
    this.publisher = publisher;
  }

  @Override
  public void fire() {
    publisher.publish(this);
  }
}
