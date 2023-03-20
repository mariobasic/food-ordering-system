package com.food.ordering.system.order.service.dataaccess.order.adapter;

import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderJpaRepository repository;
  private final OrderDataAccessMapper mapper;

  @Override
  public Order save(Order order) {

    return mapper.orderEntityToOrder(
        repository.save(mapper.orderToOrderEntity(order)));
  }

  @Override
  public Optional<Order> findByTrackingId(TrackingId trackingId) {

    return repository.findByTrackingId(trackingId.getValue())
        .map(mapper::orderEntityToOrder);
  }
}
