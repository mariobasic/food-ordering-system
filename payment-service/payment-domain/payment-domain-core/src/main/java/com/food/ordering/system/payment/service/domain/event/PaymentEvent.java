package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvents;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class PaymentEvent implements DomainEvents<Payment> {

  private final Payment payment;
  private final ZonedDateTime createdAt;
  private final List<String> failureMessages;

}
