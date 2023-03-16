package com.food.ordering.system.application.exception.handler;

import lombok.Builder;

@Builder
public record ErrorDto(
    String code,
    String message
) {



}
