package com.jkweg.smartwallet.exception;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception){

        Map<String,String> hashMap = new HashMap<>();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();

        fieldErrorList.forEach(fieldError -> {
            hashMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });

        return hashMap;
    }
}
