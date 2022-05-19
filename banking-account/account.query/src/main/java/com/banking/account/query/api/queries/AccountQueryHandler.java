package com.banking.account.query.api.queries;

import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountQueryHandler implements QueryHandler{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> accounts = accountRepository.findAll();
        //PARSEAR necesito devolver lista de base entities
        List<BaseEntity> bankAccountList = new ArrayList<>();
        accounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());
        if (bankAccount.isEmpty()){
            return null;
        }
        return List.of(bankAccount.get());
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccount = accountRepository.findByAccountHolder(query.getAccountHolder());
        if (bankAccount.isEmpty()){
            return null;
        }
        return List.of(bankAccount.get());
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
       //encontrar cuentas dependiendo su balance
        List<BaseEntity> bankAccountList = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());
        return bankAccountList;
    }
}
