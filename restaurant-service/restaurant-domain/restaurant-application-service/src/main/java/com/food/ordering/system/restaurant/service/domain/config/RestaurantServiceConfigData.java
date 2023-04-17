package com.food.ordering.system.restaurant.service.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "restaurant-service")
public record RestaurantServiceConfigData(
    String restaurantApprovalRequestTopicName,
    String restaurantApprovalResponseTopicName
) {

}
