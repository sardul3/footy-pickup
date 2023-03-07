package com.sardul3.footypickup.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;


//TODO: add a new error message format
//TODO: create custom exceptions and catch them here
@ControllerAdvice
public class PlayerValidationAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleValidationErrors(WebExchangeBindException exception) {
        var errors = exception
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
