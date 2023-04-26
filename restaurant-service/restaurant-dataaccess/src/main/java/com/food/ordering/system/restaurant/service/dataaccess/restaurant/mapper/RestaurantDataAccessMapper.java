package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.domain.entity.Product;
import com.food.ordering.system.restaurant.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.valueobject.OrderApprovalId;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataAccessMapper {

  public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
    return restaurant.getOrderDetail().getProducts().stream()
        .map(Product::getId)
        .map(ProductId::getValue)
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
                .available(entity.getProductAvailable())
                .build())
        .toList();

    return Restaurant.builder()
        .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
        .orderDetail(OrderDetail.builder().products(products).build())
        .active(restaurantEntity.getRestaurantActive())
        .build();
  }

  public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
    return OrderApprovalEntity.builder()
        .id(orderApproval.getId().getValue())
        .restaurantId(orderApproval.getRestaurantId().getValue())
        .orderId(orderApproval.getOrderId().getValue())
        .status(orderApproval.getApprovalStatus())
        .build();
  }

  public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity entity) {
    return OrderApproval.builder()
        .orderApprovalId(new OrderApprovalId(entity.getId()))
        .restaurantId(new RestaurantId(entity.getRestaurantId()))
        .orderId(new OrderId(entity.getOrderId()))
        .approvalStatus(entity.getStatus())
        .build();
  }

}
