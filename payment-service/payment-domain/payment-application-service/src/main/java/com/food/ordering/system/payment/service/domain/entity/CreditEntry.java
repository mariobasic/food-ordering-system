package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId;
import lombok.Builder;
import lombok.Getter;

/**
 * We can think of this as a separate Aggregate Root. If we had API to top up credits for a customer
 * so that they can act independently of a payment they would be Aggregate Root.
 */
@Getter
public class CreditEntry extends BaseEntity<CreditEntryId> {

  private final CustomerId customerId;
  private Money totalCreditAmount;


  public void addCreditAmount(Money amount) {
    this.totalCreditAmount = totalCreditAmount.add(amount);
  }

  public void subtractCreditAmount(Money amount) {

    this.totalCreditAmount = totalCreditAmount.subtract(amount);
  }

  @Builder
  public CreditEntry(CreditEntryId creditEntryId, CustomerId customerId, Money totalCreditAmount) {
    this.setId(creditEntryId);
    this.customerId = customerId;
    this.totalCreditAmount = totalCreditAmount;
  }
}
