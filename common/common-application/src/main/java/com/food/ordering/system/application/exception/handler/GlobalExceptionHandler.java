package com.food.ordering.system.application.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto handleException(Exception exception) {

    log.error(exception.getMessage(), exception);

    return ErrorDto.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message("Unexpected error!")
        .build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleException(ConstraintViolationException exception) {

    log.error(exception.getMessage(), exception);

    String violations = exception.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining("--"));

    return ErrorDto.builder()
        .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(violations)
        .build();
  }

}
