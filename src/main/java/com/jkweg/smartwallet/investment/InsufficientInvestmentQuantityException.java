package com.jkweg.smartwallet.investment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InsufficientInvestmentQuantityException extends RuntimeException {

    InsufficientInvestmentQuantityException() {
        super("U couldn't sell more than u have!");
    }
}
