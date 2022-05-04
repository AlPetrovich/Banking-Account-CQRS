package com.banking.account.cmd.api.controllers;
import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/openBankAccount")
public class OpenAccountController {

    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    //el cliente genera el objeto comando para abrir una cuenta
    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        var id = UUID.randomUUID().toString(); //genere un id aleatorio
        openAccountCommand.setId(id);
        try{
            commandDispatcher.send(openAccountCommand);
            return new ResponseEntity<>(new OpenAccountResponse("Cuenta bancaria creada con exito", id), HttpStatus.CREATED);

        }catch(IllegalStateException e){
            logger.log(Level.WARNING, "Error al crear la cuenta bancaria", e.toString());
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            var errorMessage = MessageFormat.format("Error durante proceso de request {0}", id);
            logger.log(Level.SEVERE, errorMessage, e.toString());
            return new ResponseEntity<>(new OpenAccountResponse(errorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
