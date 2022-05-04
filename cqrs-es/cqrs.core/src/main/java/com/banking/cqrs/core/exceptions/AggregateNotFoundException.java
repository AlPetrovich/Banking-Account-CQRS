package com.banking.cqrs.core.exceptions;

public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException(String aggregateId) {
        super(String.format("Aggregate with id %s not found", aggregateId));
    }
}
