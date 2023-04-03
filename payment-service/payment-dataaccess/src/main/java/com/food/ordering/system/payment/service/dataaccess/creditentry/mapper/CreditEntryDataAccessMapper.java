package com.food.ordering.system.payment.service.dataaccess.creditentry.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

  public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity entity) {
    return CreditEntry.builder()
        .creditEntryId(new CreditEntryId(entity.getId()))
        .customerId(new CustomerId(entity.getCustomerId()))
        .totalCreditAmount(new Money(entity.getTotalCreditAmount()))
        .build();
  }

  public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
    return CreditEntryEntity.builder()
        .id(creditEntry.getId().getValue())
        .customerId(creditEntry.getCustomerId().getValue())
        .totalCreditAmount(creditEntry.getTotalCreditAmount().amount())
        .build();
  }

}
