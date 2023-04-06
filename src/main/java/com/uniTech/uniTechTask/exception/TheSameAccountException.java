package com.uniTech.uniTechTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TheSameAccountException extends RuntimeException {
    public TheSameAccountException(String message) {
        super(message);
    }
}
