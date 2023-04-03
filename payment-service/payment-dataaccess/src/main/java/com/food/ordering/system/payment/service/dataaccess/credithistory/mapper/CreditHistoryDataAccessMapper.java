package com.food.ordering.system.payment.service.dataaccess.credithistory.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {

  public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity entity) {
    return CreditHistory.builder()
        .creditHistoryId(new CreditHistoryId(entity.getId()))
        .customerId(new CustomerId(entity.getCustomerId()))
        .amount(new Money(entity.getAmount()))
        .transactionType(entity.getType())
        .build();
  }

  public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
    return CreditHistoryEntity.builder()
        .id(creditHistory.getId().getValue())
        .customerId(creditHistory.getCustomerId().getValue())
        .amount(creditHistory.getAmount().amount())
        .type(creditHistory.getTransactionType())
        .build();
  }
}
