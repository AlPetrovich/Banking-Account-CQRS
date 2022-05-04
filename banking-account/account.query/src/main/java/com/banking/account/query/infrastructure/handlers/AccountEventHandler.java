package com.banking.account.query.infrastructure.handlers;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        //crear la cuenta
        var bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        //persistimos en mysql
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if(bankAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        //obtengo el balance actual
        var currentBalance = bankAccount.get().getBalance();
        //actualizo el balance con el deposito del cliente
        var newBalance = currentBalance + event.getAmount();
        //actualizo el balance en la base de datos
        bankAccount.get().setBalance(newBalance);
        //persistimos en mysql
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if(bankAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        //obtengo el balance actual
        var currentBalance = bankAccount.get().getBalance();
        //actualizo el balance con el deposito del cliente
        var newBalance = currentBalance - event.getAmount();
        //actualizo el balance en la base de datos
        bankAccount.get().setBalance(newBalance);
        //persistimos en mysql
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if(bankAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        //eliminamos la cuenta
        accountRepository.delete(bankAccount.get());
    }
}
