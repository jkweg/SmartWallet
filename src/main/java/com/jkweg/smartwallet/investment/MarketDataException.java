package com.jkweg.smartwallet.investment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
class MarketDataException extends RuntimeException {

    MarketDataException(String message) {
        super(message);
    }
}
