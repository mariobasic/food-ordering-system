package com.food.ordering.system.payment.service.domain;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  public PaymentDomainService paymentDomainService() {
    return new PaymentDomainServiceImpl();
  }

}
