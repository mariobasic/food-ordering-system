package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.Product;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderMessagingDataMapper {

  public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(
      OrderCreatedEvent event) {

    Order order = event.getOrder();

    return PaymentRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setCustomerId(order.getCustomerId().getValue().toString())
        .setOrderId(order.getId().getValue().toString())
        .setPrice(order.getPrice().amount())
        .setCreatedAt(event.getCreatedAt().toInstant())
        .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
        .build();
  }

  public PaymentRequestAvroModel orderCanceledEventToPaymentRequestAvroModel(
      OrderCancelledEvent event) {

    Order order = event.getOrder();

    return PaymentRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setCustomerId(order.getCustomerId().getValue().toString())
        .setOrderId(order.getId().getValue().toString())
        .setPrice(order.getPrice().amount())
        .setCreatedAt(event.getCreatedAt().toInstant())
        .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
        .build();
  }

  public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(
      OrderPaidEvent event) {

    Order order = event.getOrder();

    return RestaurantApprovalRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(event.getOrder().getId().getValue().toString())
        .setRestaurantId(event.getOrder().getRestaurantId().getValue().toString())
        .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
        .setProducts(
            order.getItems().stream()
                .map(OrderMessagingDataMapper::getAvroProductModel)
                .toList()
        )
        .setPrice(order.getPrice().amount())
        .setCreatedAt(event.getCreatedAt().toInstant())
        .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
        .build();
  }

  private static Product getAvroProductModel(OrderItem orderItem) {
    return Product.newBuilder()
        .setId(orderItem.getProduct().getId().getValue().toString())
        .setQuantity(orderItem.getQuantity())
        .build();
  }

  public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel model) {
    return PaymentResponse.builder()
        .id(model.getId())
        .sagaId(model.getSagaId())
        .paymentId(model.getPaymentId())
        .customerId(model.getCustomerId())
        .orderId(model.getOrderId())
        .price(model.getPrice())
        .createdAt(model.getCreatedAt())
        .paymentStatus(PaymentStatus.valueOf(model.getPaymentStatus().name()))
        .failureMessages(model.getFailureMessages())
        .build();
  }

  public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(
      RestaurantApprovalResponseAvroModel model) {

    return RestaurantApprovalResponse.builder()
        .id(model.getId())
        .sagaId(model.getSagaId())
        .restaurantId(model.getRestaurantId())
        .orderId(model.getOrderId())
        .createdAt(model.getCreatedAt())
        .orderApprovalStatus(OrderApprovalStatus.valueOf(model.getOrderApprovalStatus().name()))
        .failureMessages(model.getFailureMessages())
        .build();
  }
}

