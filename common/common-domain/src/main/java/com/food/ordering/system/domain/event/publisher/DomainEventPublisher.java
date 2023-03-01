package com.food.ordering.system.domain.event.publisher;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.event.DomainEvents;
import com.food.ordering.system.domain.valueobject.BaseId;

public interface DomainEventPublisher<T extends DomainEvents<? extends AggregateRoot<? extends BaseId<?>>>> {

  void publish(T domainEvent);
}
