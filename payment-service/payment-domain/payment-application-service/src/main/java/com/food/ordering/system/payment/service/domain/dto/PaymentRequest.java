package com.food.ordering.system.payment.service.domain.dto;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record PaymentRequest(
    String id,
    String sagaId,
    String orderId,
    String customerId,
    BigDecimal price,
    Instant createdAt,
    // should this be modifiable?
    PaymentOrderStatus paymentOrderStatus
) {

}
