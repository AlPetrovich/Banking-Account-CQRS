package com.banking.account.query.infrastructure;
import com.banking.cqrs.core.domain.BaseEntity;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import com.banking.cqrs.core.queries.BaseQuery;
import com.banking.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {
    //lista de rutas
    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    //registrar los handlers dentro de las rutas = commands
    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        //obtenes lista de handlers
        var handlers = routes.get(query.getClass());
        //si no hay handlers
        if (handlers == null || handlers.size() <= 0) {
            throw new RuntimeException("No handler found for query: " + query.getClass());
        }
        if (handlers.size() > 1) {
            throw new RuntimeException("Multiple handlers found for query: " + query.getClass());
        }
        //obtener el handler
        var handler = handlers.get(0);
        //ejecutar el handler
        return handler.handle(query);
    }
}
