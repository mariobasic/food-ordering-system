package com.food.ordering.system.order.service.dataaccess.customer.adapter;

import com.food.ordering.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

  private final CustomerJpaRepository repository;
  private final CustomerDataAccessMapper mapper;

  @Override
  public Optional<Customer> findCustomer(UUID customerId) {

    return repository.findById(customerId)
        .map(mapper::customerEntityToCustomer);
  }
}