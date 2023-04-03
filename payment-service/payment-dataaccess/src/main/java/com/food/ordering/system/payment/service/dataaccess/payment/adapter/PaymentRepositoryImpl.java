package com.food.ordering.system.payment.service.dataaccess.payment.adapter;

import com.food.ordering.system.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

  private final PaymentJpaRepository repository;
  private final PaymentDataAccessMapper mapper;

  @Override
  public Payment save(Payment payment) {
    return mapper.paymentEntityToPayment(
        repository.save(mapper.paymentToPaymentEntity(payment)));
  }

  @Override
  public Optional<Payment> findByOrderId(UUID orderId) {
    return repository.findByOrderId(orderId)
        .map(mapper::paymentEntityToPayment);
  }
}
