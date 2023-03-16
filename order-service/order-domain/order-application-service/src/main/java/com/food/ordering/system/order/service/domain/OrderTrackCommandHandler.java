package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

  private final OrderDataMapper mapper;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public TrackOrderResponse trackOrder(TrackOrderQuery query) {
    Order orderResult = getTrackingOrder(query.orderTrackingId());

    return mapper.toTrackOrderResponseFrom(orderResult);
  }

  private Order getTrackingOrder(UUID orderTrackingId) {

    return orderRepository.findByTrackingId(new TrackingId(orderTrackingId))
        .orElseThrow(() -> {
          log.warn("Could not find order with tracking id '{}'", orderTrackingId);
          throw new OrderNotFoundException(
              "Could not find order with tracking id: " + orderTrackingId);
        });
  }


}
