package com.food.ordering.system.domain.entity;


import lombok.Getter;
import lombok.Setter;

public abstract class BaseEntity<ID> {

  @Getter
  @Setter
  private ID id;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseEntity<?> that = (BaseEntity<?>) o;

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
