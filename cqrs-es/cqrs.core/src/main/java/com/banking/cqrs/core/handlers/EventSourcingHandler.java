package com.banking.cqrs.core.handlers;
import com.banking.cqrs.core.domain.AggregateRoot;

//puedo implementar mis handlers concretos
public interface EventSourcingHandler<T> {

    void save(AggregateRoot aggregateRoot);

    T getById(String id); // representa al aggregate id que quiero buscar

}
