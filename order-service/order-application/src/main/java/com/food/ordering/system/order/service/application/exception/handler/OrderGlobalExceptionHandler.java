package com.food.ordering.system.order.service.application.exception.handler;

import com.food.ordering.system.application.exception.handler.ErrorDto;
import com.food.ordering.system.application.exception.handler.GlobalExceptionHandler;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {


  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(OrderDomainException.class)
  public ErrorDto handleException(OrderDomainException exception) {

    log.error(exception.getMessage(), exception);

    return ErrorDto.builder()
        .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(exception.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(OrderNotFoundException.class)
  public ErrorDto handleException(OrderNotFoundException exception) {

    log.error(exception.getMessage(), exception);

    return ErrorDto.builder()
        .code(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(exception.getMessage())
        .build();
  }



}
