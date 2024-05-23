package com.microservice1.microservice1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

@Configuration
public class DatabaseConfig {


    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }
}
