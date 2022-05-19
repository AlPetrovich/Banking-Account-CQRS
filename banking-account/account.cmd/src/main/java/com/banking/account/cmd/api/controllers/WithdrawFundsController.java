package com.banking.account.cmd.api.controllers;
import com.banking.account.cmd.api.command.DepositFundsCommand;
import com.banking.account.cmd.api.command.WithdrawFundsCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/withDrawFunds")
public class WithdrawFundsController {

    private final Logger logger = Logger.getLogger(WithdrawFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;


    @PutMapping("/{accountId}")
    public ResponseEntity<BaseResponse> withDrawFunds(@PathVariable("accountId") String accountId,
                                                     @RequestBody WithdrawFundsCommand command) {
        try{
            command.setId(accountId);
            commandDispatcher.send(command);
            return ResponseEntity.ok(new BaseResponse(MessageFormat.format("Withdraw funds for account {0}", accountId)));
        }catch(IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Error while withdrawing funds for account {0}", accountId), e);
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage()));
        }catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while withdrawing funds for account {0}", accountId);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.internalServerError().body(new BaseResponse(safeErrorMessage));
        }
    }
}
