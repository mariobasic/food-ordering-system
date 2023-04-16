package com.food.ordering.system.payment.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.food.ordering.system.payment.service.dataaccess")
@EntityScan(basePackages = "com.food.ordering.system.payment.service.dataaccess")
@ConfigurationPropertiesScan(basePackages = "com.food.ordering.system")
@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
public class PaymentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentServiceApplication.class, args);
  }
}
