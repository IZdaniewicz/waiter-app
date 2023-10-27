package com.example.demo.cqrs.QueryBusImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryBus {
    private final Map<Class<?>, QueryHandlerInterface<?, ?>> queryHandlers;

    @Autowired
    public QueryBus(List<QueryHandlerInterface<?, ?>> queryHandlerList) {
        this.queryHandlers = new HashMap<>();
        for (QueryHandlerInterface<?, ?> handler : queryHandlerList) {
            Class<?> queryClass = getQueryClass(handler);
            if (queryClass != null) {
                queryHandlers.put(queryClass, handler);
            } else {
                throw new IllegalArgumentException("Unable to resolve query type.");
            }
        }
    }

    private Class<?> getQueryClass(QueryHandlerInterface<?, ?> handler) {
        // Extract the query class from a custom annotation
        QueryHandler annotation = handler.getClass().getAnnotation(QueryHandler.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    public <R> R dispatch(Query query) {
        QueryHandlerInterface<Query, R> handler = (QueryHandlerInterface<Query, R>) queryHandlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for the query.");
        }
        return handler.handle(query);
    }
}