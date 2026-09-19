package com.jkweg.smartwallet.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long id){
        super("Could not find transaction with this id: " + id);
    }
}
