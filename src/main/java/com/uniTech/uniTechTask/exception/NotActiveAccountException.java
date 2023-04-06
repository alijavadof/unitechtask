package com.uniTech.uniTechTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)

public class NotActiveAccountException extends RuntimeException {
    public NotActiveAccountException(String message) {
        super(message);
    }
}
