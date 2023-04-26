package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.restaurant.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
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
    List<UUID> products = mapper.restaurantToRestaurantProducts(restaurant);

    return repository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), products)
        .map(mapper::restaurantEntityToRestaurant);
  }
}