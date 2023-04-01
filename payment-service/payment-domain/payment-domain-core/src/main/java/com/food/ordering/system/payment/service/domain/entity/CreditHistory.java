package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import lombok.Builder;
import lombok.Getter;

/**
 * We can think of this as a separate Aggregate Root. If we had API to top up credits for a customer
 * so that they can act independently of a payment they would be Aggregate Root.
 */
@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {

  public final CustomerId customerId;
  public final Money amount;
  public final TransactionType transactionType;


  @Builder
  public CreditHistory(CreditHistoryId creditHistoryId, CustomerId customerId, Money amount,
      TransactionType transactionType) {
    this.setId(creditHistoryId);
    this.customerId = customerId;
    this.amount = amount;
    this.transactionType = transactionType;
  }
}
