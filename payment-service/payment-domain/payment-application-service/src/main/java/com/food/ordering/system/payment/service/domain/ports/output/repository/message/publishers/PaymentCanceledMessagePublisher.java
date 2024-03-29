package com.food.ordering.system.payment.service.domain.ports.output.repository.message.publishers;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.event.PaymentCanceledEvent;

public interface PaymentCanceledMessagePublisher extends
    DomainEventPublisher<PaymentCanceledEvent> {

}
