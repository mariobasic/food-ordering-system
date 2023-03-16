package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderTestConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS) // by default new instance of the class is created
public class OrderApplicationServiceTest {

  @Autowired
  private OrderApplicationService orderApplicationService;

  @Autowired
  private OrderDataMapper orderDataMapper;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  private CreateOrderCommand createOrderCommand;
  private CreateOrderCommand createOrderCommandWrongPrice;
  private CreateOrderCommand createOrderCommandWrongProductPrice;
  public static final UUID CUSTOMER_ID = UUID.fromString("90e8e857-1673-49b2-8943-68c4936c90fa");
  public static final UUID RESTAURANT_ID = UUID.fromString("d72edd7c-382b-44ad-a6f8-dd78fa152636");
  public static final UUID PRODUCT1_ID = UUID.fromString("22b7ea19-abb0-4db9-a4f8-edba8327228a");
  public static final UUID PRODUCT2_ID = UUID.fromString("8f4bb051-eda4-419a-aa57-dbb0040dd6c0");
  public static final UUID ORDER_ID = UUID.fromString("2b9bca0a-08de-4845-8981-bd5a2457d497");
  public static final BigDecimal PRICE = new BigDecimal("200.00");

  @BeforeEach
  public void init() {
    createOrderCommand = EntityBuilder.createOrderCommand();

    createOrderCommandWrongPrice = EntityBuilder.createOrderCommandWrongPrice();

    createOrderCommandWrongProductPrice = EntityBuilder.createOrderCommandWrongProductPrice();

    Customer customer = new Customer();
    customer.setId(new CustomerId(CUSTOMER_ID));

    Restaurant restaurantResponse = EntityBuilder.activeRestaurant();

    Order order = orderDataMapper.toOrderFrom(createOrderCommand);
    order.setId(new OrderId(ORDER_ID));

    when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(
        Optional.of(customer));

    when(restaurantRepository.findRestaurantInformation(
        orderDataMapper.toRestaurantFrom(createOrderCommand)))
        .thenReturn(Optional.of(restaurantResponse));

    when(orderRepository.save(any(Order.class))).thenReturn(order);
  }

  @Test
  public void givenCorrectCreateOrderCommand_WhenCreateOrder_ThanCreateOrder() {
    CreateOrderResponse order = orderApplicationService.createOrder(createOrderCommand);
    assertEquals(OrderStatus.PENDING, order.orderStatus());
    assertEquals("Order created successfully", order.message());
    assertNotNull(order.orderTrackingId());
  }

  @Test
  public void givenWrongPriceCreateOrderCommand_WhenCreateOrder_ThenThrow() {
    OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () ->
        orderApplicationService.createOrder(createOrderCommandWrongPrice));

    assertEquals("Total price: 250.00  is not equal to Order items total: 200.00!",
        orderDomainException.getMessage());
  }

  @Test
  public void givenWrongProductPrice_WhenCreateOrder_ThanThrow() {
    OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () ->
        orderApplicationService.createOrder(createOrderCommandWrongProductPrice));

    assertEquals("Order item price: 60.00 is not valid for product " + PRODUCT1_ID,
        orderDomainException.getMessage());
  }

  @Test
  public void givenInactiveRestaurant_WhenCreateOrder_ThanThrow() {
    Restaurant restaurantResponse = EntityBuilder.inactiveRestaurant();
    when(restaurantRepository.findRestaurantInformation(any(Restaurant.class)))
        .thenReturn(Optional.of(restaurantResponse));

    OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () ->
        orderApplicationService.createOrder(createOrderCommand));

    assertEquals("Restaurant with id " + RESTAURANT_ID + " is currently not active!",
        orderDomainException.getMessage());
  }
}
