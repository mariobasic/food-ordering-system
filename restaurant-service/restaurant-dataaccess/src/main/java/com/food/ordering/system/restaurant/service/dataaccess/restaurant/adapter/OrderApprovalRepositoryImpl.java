package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.restaurant.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.repository.OrderApprovalJpaRepository;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

  private final OrderApprovalJpaRepository repository;
  private final RestaurantDataAccessMapper mapper;

  @Override
  public OrderApproval save(OrderApproval orderApproval) {

    return mapper.orderApprovalEntityToOrderApproval(
        repository.save(mapper.orderApprovalToOrderApprovalEntity(orderApproval)));
  }
}
