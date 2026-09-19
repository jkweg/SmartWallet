package com.jkweg.smartwallet.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("Date range is invalid");
    }
}
