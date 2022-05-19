package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.CloseAccountCommand;
import com.banking.account.cmd.api.command.DepositFundsCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseAccountController {

    private final Logger logger = Logger.getLogger(CloseAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;


    @DeleteMapping("/{accountId}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable("accountId") String accountId) {
        try{
            commandDispatcher.send(new CloseAccountCommand(accountId));
            return ResponseEntity.ok(new BaseResponse(MessageFormat.format("Account {0} closed successfully", accountId)));
        }catch(IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Account {0} not found", accountId));
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage()));
        }catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error occurred while closing account {0}", accountId);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.badRequest().body(new BaseResponse(safeErrorMessage));
        }
    }
}
