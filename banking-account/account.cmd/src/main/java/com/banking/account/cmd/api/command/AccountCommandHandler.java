package com.banking.account.cmd.api.command;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {

    //necesito el event sourcing para guardar el estado de un aggregate en cada uno de estos metodos
    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command); //instancio un aggregate de tipo OpenAccountCommand
        eventSourcingHandler.save(aggregate); //guardo el aggregate en el event sourcing con sus nuevas caracteristicas

    }

    @Override
    public void handle(DepositFundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId()); //obtengo el aggregate por su id
        aggregate.depositFunds(command.getAmount()); //le agrego el monto al aggregate
        eventSourcingHandler.save(aggregate); //guardo el aggregate en el event sourcing con sus nuevas caracteristicas
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        if (command.getAmount() > aggregate.getBalance()) {
            throw new IllegalArgumentException("No se puede retirar más dinero de lo que hay en la cuenta");
        }
        aggregate.withdrawFunds(command.getAmount()); //le resto el monto al aggregate
        eventSourcingHandler.save(aggregate);
    }

    //ver close account
    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.close();
        eventSourcingHandler.save(aggregate);
    }
}
