package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.payment.service.domain.entity.Payment;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PaymentCanceledEvent extends PaymentEvent{

  public PaymentCanceledEvent(Payment payment,
      ZonedDateTime createdAt) {
    super(payment, createdAt, new ArrayList<>());
  }
}
