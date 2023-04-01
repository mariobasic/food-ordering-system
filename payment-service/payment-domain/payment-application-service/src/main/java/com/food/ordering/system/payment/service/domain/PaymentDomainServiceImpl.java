package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCanceledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

import static com.food.ordering.system.domain.DomainConstants.UTC_ZONE;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {

  @Override
  public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages) {

    payment.validatePayment(failureMessages);
    payment.initializePayment();
    validateCreditEntry(payment, creditEntry, failureMessages);
    subtractCreditEntry(payment, creditEntry);
    updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
    validateCreditHistory(creditEntry, creditHistories, failureMessages);

    if (failureMessages.isEmpty()) {
      log.info("Payment is initialized for order with id '{}'", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.COMPLETED);

      return new PaymentCompletedEvent(payment, ZonedDateTime.now(UTC_ZONE));

    }

    log.info("Payment initialization failed for order with id '{}'",
        payment.getOrderId().getValue());
    payment.updateStatus(PaymentStatus.FAILED);

    return new PaymentFailedEvent(payment, ZonedDateTime.now(UTC_ZONE), failureMessages);
  }

  private void validateCreditEntry(Payment payment, CreditEntry creditEntry,
      List<String> failureMessages) {
    if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
      log.error("Customer with id '{}' doesn't have enough credit for payment!",
          payment.getCustomerId().getValue());

      failureMessages.add("Customer with id=" + payment.getCustomerId().getValue()
          + " doesn't have enough credit for payment!");
    }
  }

  private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.subtractCreditAmount(payment.getPrice());
  }

  private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
      List<String> failureMessages) {

    Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
    Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

    if (totalDebitHistory.isGreaterThan(totalDebitHistory)) {
      log.error("Customer with id '{}' doesn't have enough credit according to credit history",
          creditEntry.getCustomerId().getValue());
      failureMessages.add("Customer with id=" + creditEntry.getCustomerId().getValue() +
          " doesn't have enough credit according to credit history");
    }

    if (!creditEntry.getTotalCreditAmount()
        .equals(totalCreditHistory.subtract(totalDebitHistory))) {
      log.error("Credit history total is not equal to current credit for customer with id '{}'",
          creditEntry.getCustomerId().getValue());

      failureMessages.add(
          "Credit history total is not equal to current credit for customer with id: "
              + creditEntry.getCustomerId().getValue());
    }

  }

  private static Money getTotalHistoryAmount(List<CreditHistory> creditHistories,
      TransactionType transactionType) {
    return creditHistories.stream()
        .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
        .map(CreditHistory::getAmount)
        .reduce(Money.ZERO, Money::add);
  }

  @Override
  public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages) {

    payment.validatePayment(failureMessages);
    addCreditEntry(payment, creditEntry);
    updateCreditHistory(payment,creditHistories, TransactionType.CREDIT);

    if (failureMessages.isEmpty()) {
      log.info("Payment is cancelled for order with id '{}'", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.CANCELED);
      return new PaymentCanceledEvent(payment, ZonedDateTime.now(UTC_ZONE));
    }

    log.info("Payment cancellation failed for order with id '{}'", payment.getOrderId().getValue());
    return new PaymentFailedEvent(payment, ZonedDateTime.now(UTC_ZONE), failureMessages);
  }

  private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.addCreditAmount(payment.getPrice());
  }

  private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories,
      TransactionType transactionType) {

    creditHistories.add(
        CreditHistory.builder()
            .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
            .customerId(payment.getCustomerId())
            .amount(payment.getPrice())
            .transactionType(transactionType)
            .build());

  }
}
