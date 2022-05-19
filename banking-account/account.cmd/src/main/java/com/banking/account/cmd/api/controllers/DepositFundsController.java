package com.banking.account.cmd.api.controllers;
import com.banking.account.cmd.api.command.DepositFundsCommand;
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
@RequestMapping("/api/vs/depositFunds")
public class DepositFundsController {

    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;


    @PutMapping("/{accountId}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable("accountId") String accountId,
                                                     @RequestBody DepositFundsCommand command) {
        try{
            command.setId(accountId);
            commandDispatcher.send(command);
            return ResponseEntity.ok(new BaseResponse("Successfully deposited funds"));
        }catch(IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Error occurred while depositing funds for account {0}", accountId), e);
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage()));
        }catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error occurred while depositing funds for account {0}", accountId);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.badRequest().body(new BaseResponse(safeErrorMessage));
        }
    }
}
