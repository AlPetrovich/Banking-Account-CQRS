package com.banking.account.cmd.api.command;
import com.banking.account.common.dto.AccountType;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    //Nombre de la persona a la que se le crea la cuenta
    private String accountHolder;
    //Tipo de cuenta- dto account.common
    private AccountType accountType;
    private double openingBalance;

}
