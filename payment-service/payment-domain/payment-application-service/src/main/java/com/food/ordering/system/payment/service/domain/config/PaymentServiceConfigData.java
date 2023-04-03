package com.food.ordering.system.payment.service.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-service")
public record PaymentServiceConfigData(String paymentRequestTopicName,
                                       String paymentResponseTopicName) {


}
