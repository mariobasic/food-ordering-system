package com.food.ordering.system.restaurant.domain.event;

import com.food.ordering.system.domain.event.DomainEvents;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.entity.OrderApproval;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class OrderApprovalEvent implements DomainEvents<OrderApproval> {

  private final RestaurantId restaurantId;
  private final OrderApproval orderApproval;
  private final ZonedDateTime createdAt;
  private final List<String> failureMessages;

}
