package com.jkweg.smartwallet.investment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class InvestmentTransactionNotFoundException extends RuntimeException {

    InvestmentTransactionNotFoundException(Long id) {
        super("Could not find investment transaction with this id: " + id);
    }
}
