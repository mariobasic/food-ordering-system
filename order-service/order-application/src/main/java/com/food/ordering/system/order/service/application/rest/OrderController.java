package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

  private final OrderApplicationService orderApplicationService;

  @PostMapping
  public ResponseEntity<CreateOrderResponse> createOrder(
      @RequestBody CreateOrderCommand command) {

    log.info("Create order for customer '{}' at restaurant '{}'", command.customerId(),
        command.restaurantId());
    CreateOrderResponse response = orderApplicationService.createOrder(command);
    log.info("Order created with tracking id '{}'", response.orderTrackingId());

    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{trackingId}")
  public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {

    TrackOrderResponse response = orderApplicationService.trackOrder(
        new TrackOrderQuery(trackingId));

    log.info("Returning order status with tracking id '{}'", response.orderTrackingId());

    return ResponseEntity.ok(response);
  }


}
