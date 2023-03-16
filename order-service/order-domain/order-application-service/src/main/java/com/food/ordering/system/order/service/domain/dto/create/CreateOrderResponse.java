package com.food.ordering.system.order.service.domain.dto.create;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateOrderResponse(
    @NotNull UUID orderTrackingId,
    @NotNull OrderStatus orderStatus,
    @NotNull String message) {

}
