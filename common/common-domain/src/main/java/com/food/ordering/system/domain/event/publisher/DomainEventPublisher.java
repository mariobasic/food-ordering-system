package com.food.ordering.system.domain.event.publisher;

import com.food.ordering.system.domain.event.DomainEvents;
// restaurant service publishes an event that is not an aggregate root :think
//public interface DomainEventPublisher<T extends DomainEvents<? extends AggregateRoot<? extends BaseId<?>>>> {
public interface DomainEventPublisher<T extends DomainEvents<?>> {

  void publish(T domainEvent);
}
