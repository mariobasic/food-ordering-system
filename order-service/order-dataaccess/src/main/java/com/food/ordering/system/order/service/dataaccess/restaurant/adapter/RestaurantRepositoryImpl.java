package com.food.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

  private final RestaurantJpaRepository repository;
  private final RestaurantDataAccessMapper mapper;


  @Override
  public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {

    List<UUID> productIds = mapper.restaurantToRestaurantProducts(restaurant);

    return repository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), productIds)
        .map(mapper::restaurantEntityToRestaurant);

  }
}
