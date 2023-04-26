package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.domain.entity.Product;
import com.food.ordering.system.restaurant.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMessagingDataMapper {

  public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(
      RestaurantApprovalRequestAvroModel model) {

    return RestaurantApprovalRequest.builder()
        .id(model.getId())
        .sagaId(model.getSagaId())
        .restaurantId(model.getRestaurantId())
        .orderId(model.getOrderId())
        .restaurantOrderStatus(
            RestaurantOrderStatus.valueOf(model.getRestaurantOrderStatus().name()))
        .products(
            model.getProducts().stream()
                .map(avroModel ->
                    Product.builder()
                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                        .quantity(avroModel.getQuantity())
                        .build())
                .toList())
        .price(model.getPrice())
        .createdAt(model.getCreatedAt())
        .build();
  }

  public RestaurantApprovalResponseAvroModel orderApprovedEventToRestaurantApprovalResponseAvroModel(
      OrderApprovedEvent event) {

    return RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(event.getOrderApproval().getOrderId().getValue().toString())
        .setRestaurantId(event.getRestaurantId().getValue().toString())
        .setCreatedAt(event.getCreatedAt().toInstant())
        .setOrderApprovalStatus(
            OrderApprovalStatus.valueOf(event.getOrderApproval().getApprovalStatus().name()))
        .setFailureMessages(event.getFailureMessages())
        .build();
  }

  public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(
      OrderRejectedEvent event) {

    return RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(event.getOrderApproval().getOrderId().getValue().toString())
        .setRestaurantId(event.getRestaurantId().getValue().toString())
        .setCreatedAt(event.getCreatedAt().toInstant())
        .setOrderApprovalStatus(
            OrderApprovalStatus.valueOf(event.getOrderApproval().getApprovalStatus().name()))
        .setFailureMessages(event.getFailureMessages())
        .build();
  }
}