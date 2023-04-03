package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataMapper {


  public Payment paymentRequestModelToPayment(PaymentRequest request) {

    return Payment.builder()
        .orderId(new OrderId(UUID.fromString(request.orderId())))
        .paymentId(new PaymentId(UUID.fromString(request.id())))
        .customerId(new CustomerId(UUID.fromString(request.customerId())))
        .build();
  }
}
