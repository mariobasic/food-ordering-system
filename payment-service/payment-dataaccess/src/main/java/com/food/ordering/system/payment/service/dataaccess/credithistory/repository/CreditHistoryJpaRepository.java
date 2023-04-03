package com.food.ordering.system.payment.service.dataaccess.credithistory.repository;

import com.food.ordering.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {

  Optional<List<CreditHistoryEntity>> findByCustomerId(UUID customerId);
}
