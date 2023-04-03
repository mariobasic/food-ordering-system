package com.food.ordering.system.payment.service.dataaccess.creditentry.adapter;

import com.food.ordering.system.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

  private final CreditEntryJpaRepository repository;
  private final CreditEntryDataAccessMapper mapper;

  @Override
  public CreditEntry save(CreditEntry creditEntry) {
    return mapper.creditEntryEntityToCreditEntry(
        repository.save(mapper.creditEntryToCreditEntryEntity(creditEntry)));
  }

  @Override
  public Optional<CreditEntry> findByCustomerId(UUID customerId) {
    return repository.findByCustomerId(customerId)
        .map(mapper::creditEntryEntityToCreditEntry);
  }
}
