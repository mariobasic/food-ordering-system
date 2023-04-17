package com.food.ordering.system.restaurant.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {

  public RestaurantNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public RestaurantNotFoundException(String message) {
    super(message);
  }
}
