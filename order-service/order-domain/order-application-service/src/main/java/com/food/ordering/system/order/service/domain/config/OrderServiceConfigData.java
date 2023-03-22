package com.food.ordering.system.order.service.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@Configuration
@ConfigurationProperties(prefix = "order-service")
public record OrderServiceConfigData(
    String paymentRequestTopicName,
    String paymentResponseTopicName,
    String restaurantApprovalRequestTopicName,
    String restaurantApprovalResponseTopicName
) {

}
