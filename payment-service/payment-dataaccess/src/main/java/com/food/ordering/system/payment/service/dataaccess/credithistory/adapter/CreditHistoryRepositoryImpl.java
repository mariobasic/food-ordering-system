package com.food.ordering.system.payment.service.dataaccess.credithistory.adapter;

import com.food.ordering.system.payment.service.dataaccess.credithistory.mapper.CreditHistoryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

  private final CreditHistoryDataAccessMapper mapper;
  private final CreditHistoryJpaRepository repository;

  @Override
  public CreditHistory save(CreditHistory creditHistory) {

    return mapper.creditHistoryEntityToCreditHistory(
        repository.save(mapper.creditHistoryToCreditHistoryEntity(creditHistory)));
  }

  @Override
  public Optional<List<CreditHistory>> findByCustomerId(UUID customerId) {
    return repository.findByCustomerId(customerId)
        .map(creditHistoryEntities ->
            creditHistoryEntities.stream()
                .map(mapper::creditHistoryEntityToCreditHistory)
                .toList());
  }
}
