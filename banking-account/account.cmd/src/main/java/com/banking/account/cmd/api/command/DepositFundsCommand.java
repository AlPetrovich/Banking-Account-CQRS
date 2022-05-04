package com.banking.account.cmd.api.command;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {

    //dinero a depositar
    private double amount;
}
