package com.banking.account.cmd.domain;
import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;
import java.util.Date;


//---------------AGGREGATE -> OPERACIONES CUENTA BANCARIA-----------------

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;
    private double balance;

    public double getBalance() {
        return balance;
    }

    //constructor para alta de cuenta - cliente envia el command
    public AccountAggregate(OpenAccountCommand command) {
        //para disparar evento debo llamar al raiseEvent
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    //metodo para depositar
    public void depositFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Account is inactive");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        //ejecutar evento
        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    //metodo para actualizar valores definidos en depositFunds
    public void apply(FundsDepositedEvent event){
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    //operacion para retirar dinero
    public void withdrawFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Account is inactive");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (balance < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        //ejecutar evento
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    //metodo para actualizar valores definidos en withdrawFunds
    public void apply(FundsWithdrawnEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    //cerrar cuenta de banco
    public void close() {
        if (!active) {
            throw new IllegalStateException("Account is inactive");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    //metodo para actualizar valores definidos en close
    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }
}
