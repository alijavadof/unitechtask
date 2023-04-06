package com.uniTech.uniTechTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyPairNotFoundException extends RuntimeException{
    public CurrencyPairNotFoundException(String message) {
        super(message);
    }
}
