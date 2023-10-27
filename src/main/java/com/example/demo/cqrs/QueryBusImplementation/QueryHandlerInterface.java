package com.example.demo.cqrs.QueryBusImplementation;

public interface QueryHandlerInterface<Q extends Query, R> {
    R handle(Q query);
}
