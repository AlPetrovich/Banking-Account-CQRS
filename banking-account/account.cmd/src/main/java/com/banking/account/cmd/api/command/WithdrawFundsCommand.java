package com.banking.account.cmd.api.command;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;


//retirar fondos de cuenta
@Data
public class WithdrawFundsCommand extends BaseCommand {

    private double amount;
}
