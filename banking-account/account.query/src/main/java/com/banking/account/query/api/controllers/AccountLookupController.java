package com.banking.account.query.api.controllers;
import com.banking.account.query.api.dto.AccountLookupResponse;
import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.api.queries.FindAccountByHolderQuery;
import com.banking.account.query.api.queries.FindAccountByIdQuery;
import com.banking.account.query.api.queries.FindAccountWithBalanceQuery;
import com.banking.account.query.api.queries.FindAllAccountsQuery;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Buscador de cuentas
@RestController
@RequestMapping("/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    @Autowired
    private QueryDispatcher queryDispatcher;

    //metodo devuelva todas las listas bancarias
    @GetMapping("/")
    public ResponseEntity<AccountLookupResponse> getAllBankAccounts() {
        try{
            //Cobsulta con queryDispatcher para obtener todas las cuentas
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            //valido que la lista no este vacia
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            //creo una respuesta, parseando a un tipo de dato AccountLookupResponse
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved all bank accounts")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeMessage = "Error retrieving all bank accounts";
            logger.log(Level.SEVERE, safeMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //obtener una cuenta por id
    @GetMapping("/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable("id") String id) {
        try{
            //Cobsulta con queryDispatcher para obtener todas las cuentas
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            //valido que la lista no este vacia
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            //creo una respuesta, parseando a un tipo de dato AccountLookupResponse
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved all bank accounts")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeMessage = "Error retrieving all bank accounts";
            logger.log(Level.SEVERE, safeMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //buscar cuenta por nombre de cliente
    @GetMapping("/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable("accountHolder") String accountHolder) {
        try{
            //Cobsulta con queryDispatcher para obtener todas las cuentas
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            //valido que la lista no este vacia
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            //creo una respuesta, parseando a un tipo de dato AccountLookupResponse
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved all bank accounts")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeMessage = "Error retrieving all bank accounts";
            logger.log(Level.SEVERE, safeMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //busqueda de cuenta por balance
    @GetMapping("/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountByBalance(
            @PathVariable("equalityType") EqualityType equalityType
            ,@PathVariable("balance") double balance) {
        try{
            //Cobsulta con queryDispatcher para obtener todas las cuentas
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(balance, equalityType));
            //valido que la lista no este vacia
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            //creo una respuesta, parseando a un tipo de dato AccountLookupResponse
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved all bank accounts")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeMessage = "Error retrieving all bank accounts";
            logger.log(Level.SEVERE, safeMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
