package com.banking.account.cmd.infrastructure;
import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        //esta variable contiene una lista de comandos que tienen un metodo
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        //invocamos al handler y le agregamos el parametro entrante
        handlers.add(handler); //agrego el handler al mapa
    }

    @Override
    public void send(BaseCommand command) {
        //valido si tiene handlers o no este command
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.size() == 0) {
            throw new RuntimeException("No handler for command: " + command.getClass().getName());
        }
        if(handlers.size() > 1) {
            throw new RuntimeException("Multiple handlers for command: " + command.getClass().getName());
        }
        //invoco el handler
        handlers.get(0).handle(command);
    }
}
