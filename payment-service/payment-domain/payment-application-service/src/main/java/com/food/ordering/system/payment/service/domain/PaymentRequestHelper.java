package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestHelper {

  private final PaymentDomainService paymentDomainService;
  private final PaymentDataMapper mapper;
  private final PaymentRepository paymentRepository;
  private final CreditEntryRepository creditEntryRepository;
  private final CreditHistoryRepository creditHistoryRepository;

  @Transactional
  public PaymentEvent persistPayment(PaymentRequest request) {
    log.info("Received payment complete event for order with id '{}'", request.orderId());
    Payment payment = mapper.paymentRequestModelToPayment(request);
    CreditEntry creditEntry = getCreditEntry(payment.getCustomerId().getValue());
    List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId().getValue());

    // this is dumb
    List<String> failureMessages = new ArrayList<>();

    PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment,
        creditEntry, creditHistories, failureMessages);

    persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

    return paymentEvent;
  }


  private CreditEntry getCreditEntry(UUID customerId) {

    return creditEntryRepository.findByCustomerId(customerId)
        .orElseThrow(() -> {
          log.error("Could not find credit entry for customer with id '{}'", customerId);
          return new PaymentApplicationServiceException(
              "Could not find credit entry for customer: " + customerId);

        });
  }


  private List<CreditHistory> getCreditHistories(UUID customerId) {

    return creditHistoryRepository.findByCustomerId(customerId)
        .orElseThrow(() -> {
          log.error("Could not find credit histories for customer with id '{}'", customerId);
          return new PaymentApplicationServiceException(
              "Could not find credit history for customer: " + customerId);
        });
  }

  private void persistDbObjects(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages) {

    paymentRepository.save(payment);
    if (failureMessages.isEmpty()) {
      creditEntryRepository.save(creditEntry);
      creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
    }
  }

  @Transactional
  public PaymentEvent persistCancelPayment(PaymentRequest request) {
    log.info("Received payment rollback event for order with id '{}'", request.orderId());

    Payment payment = getPayment(request.orderId());
    CreditEntry creditEntry = getCreditEntry(payment.getCustomerId().getValue());
    List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId().getValue());
    List<String> failureMessages = new ArrayList<>();
    PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry,
        creditHistories, failureMessages);

    persistDbObjects(payment,creditEntry, creditHistories, failureMessages);
    return paymentEvent;
  }

  private Payment getPayment(String orderId) {
    return paymentRepository.findByOrderId(UUID.fromString(orderId))
        .orElseThrow(() -> {
          log.error("Payment with order id '{}' could not be found!", orderId);
          return new PaymentApplicationServiceException(
              "Payment with order id: " + orderId + " could not be found!");
        });
  }
}
