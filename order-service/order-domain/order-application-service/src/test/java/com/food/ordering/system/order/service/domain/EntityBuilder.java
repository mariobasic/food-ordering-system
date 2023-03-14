package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import java.math.BigDecimal;
import java.util.List;

import static com.food.ordering.system.order.service.domain.OrderApplicationServiceTest.CUSTOMER_ID;
import static com.food.ordering.system.order.service.domain.OrderApplicationServiceTest.PRICE;
import static com.food.ordering.system.order.service.domain.OrderApplicationServiceTest.PRODUCT1_ID;
import static com.food.ordering.system.order.service.domain.OrderApplicationServiceTest.PRODUCT2_ID;
import static com.food.ordering.system.order.service.domain.OrderApplicationServiceTest.RESTAURANT_ID;

class EntityBuilder {

  public static CreateOrderCommand createOrderCommand() {
    return CreateOrderCommand.builder()
        .customerId(CUSTOMER_ID)
        .restaurantId(RESTAURANT_ID)
        .address(
            OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
        .price(PRICE)
        .items(List.of(
            OrderItem.builder()
                .productId(PRODUCT1_ID)
                .quantity(1)
                .price(new BigDecimal("50.00"))
                .subTotal(new BigDecimal("50.00"))
                .build(),
            OrderItem.builder()
                .productId(PRODUCT2_ID)
                .quantity(3)
                .price(new BigDecimal("50.00"))
                .subTotal(new BigDecimal("150.00"))
                .build()))
        .build();
  }

  public static CreateOrderCommand createOrderCommandWrongPrice() {
    return CreateOrderCommand.builder()
        .customerId(CUSTOMER_ID)
        .restaurantId(RESTAURANT_ID)
        .address(
            OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
        .price(new BigDecimal("250.00"))
        .items(List.of(
            OrderItem.builder()
                .productId(PRODUCT1_ID)
                .quantity(1)
                .price(new BigDecimal("50.00"))
                .subTotal(new BigDecimal("50.00"))
                .build(),
            OrderItem.builder()
                .productId(PRODUCT2_ID)
                .quantity(3)
                .price(new BigDecimal("50.00"))
                .subTotal(new BigDecimal("150.00"))
                .build()))
        .build();

  }

  public static CreateOrderCommand createOrderCommandWrongProductPrice() {
    return CreateOrderCommand.builder()
        .customerId(CUSTOMER_ID)
        .restaurantId(RESTAURANT_ID)
        .address(
            OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
        .price(new BigDecimal("210.00"))
        .items(List.of(
            OrderItem.builder()
                .productId(PRODUCT1_ID)
                .quantity(1)
                .price(new BigDecimal("60.00"))
                .subTotal(new BigDecimal("60.00"))
                .build(),
            OrderItem.builder()
                .productId(PRODUCT2_ID)
                .quantity(3)
                .price(new BigDecimal("50.00"))
                .subTotal(new BigDecimal("150.00"))
                .build()))
        .build();
  }

  public static Restaurant activeRestaurant() {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(RESTAURANT_ID))
        .products(List.of(
            Product.builder()
                .productId(new ProductId(PRODUCT1_ID))
                .name("product-1")
                .price(new Money(new BigDecimal("50.00")))
                .build(),
            Product.builder()
                .productId(new ProductId(PRODUCT2_ID))
                .name("product-2")
                .price(new Money(new BigDecimal("50.00")))
                .build()))
        .active(true)
        .build();
  }

  public static Restaurant inactiveRestaurant() {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(RESTAURANT_ID))
        .products(List.of(
            Product.builder()
                .productId(new ProductId(PRODUCT1_ID))
                .name("product-1")
                .price(new Money(new BigDecimal("50.00")))
                .build(),
            Product.builder()
                .productId(new ProductId(PRODUCT2_ID))
                .name("product-2")
                .price(new Money(new BigDecimal("50.00")))
                .build()))
        .active(false)
        .build();
  }


}
