package com.atalibdev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RequestValidateException extends RuntimeException{

    public RequestValidateException(String message) {
        super(message);
    }
}
