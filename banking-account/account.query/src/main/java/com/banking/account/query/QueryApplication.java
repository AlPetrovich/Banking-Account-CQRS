package com.banking.account.query;

import com.banking.account.query.api.queries.*;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	@Autowired
	private QueryDispatcher queryDispatcher;

	@Autowired
	private QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerQueryHandlers() {
		//register para el primero caso findAllAcoountsQuery, llamamos al queryHandler en su metodo handle
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler:: handle);
		queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler:: handle);
		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler:: handle);
		queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler:: handle);

	}
}
