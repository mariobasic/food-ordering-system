package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.BaseId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataAccessMapper {

  public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
    return restaurant.getProducts().stream()
        .map(BaseEntity::getId)
        .map(BaseId::getValue)
        .toList();
  }

  public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> entities) {
    RestaurantEntity restaurantEntity = entities.stream().findFirst()
        .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

    List<Product> products = entities.stream()
        .map(entity ->
            Product.builder()
                .productId(new ProductId(entity.getProductId()))
                .name(entity.getProductName())
                .price(new Money(entity.getProductPrice()))
                .build())
        .toList();

    return Restaurant.builder()
        .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
        .products(products)
        .active(restaurantEntity.getRestaurantActive())
        .build();
  }

}
