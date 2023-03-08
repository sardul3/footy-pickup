package com.sardul3.footypickup.exception;

import com.sardul3.footypickup.domain.ErrorMessage;
import com.sardul3.footypickup.domain.WarningMessage;
import com.sardul3.footypickup.exception.custom.EmptyResourceCollectionException;
import com.sardul3.footypickup.exception.custom.MatchHasInvalidNumberOfTeamsException;
import com.sardul3.footypickup.exception.custom.ResourceNotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


//TODO: add a new error message format
//TODO: create custom exceptions and catch them here
@ControllerAdvice
@Slf4j
public class PlayerValidationAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(WebExchangeBindException exception) {
        var errors = exception
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST)
                        .code("FOOTY-1002")
                        .message("Validation Error")
                        .errors(errors)
                        .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleMatchNotFound(ResourceNotFoundException exception) {
        var errors = List.of(exception
                .getLocalizedMessage());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .code("FOOTY-1000")
                .message("Resource Not Found")
                .errors(errors)
                .build();
        log.error("The match ID you provided does not exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MatchHasInvalidNumberOfTeamsException.class)
    public ResponseEntity<ErrorMessage> handleInvalidTeamsInAMatch(MatchHasInvalidNumberOfTeamsException exception) {
        var errors = List.of(exception
                .getLocalizedMessage());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .code("FOOTY-1004")
                .message("Constraint Error")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleInvalidRequestFormat(HttpMessageNotReadableException exception) {
        var errors = List.of(exception
                .getLocalizedMessage());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .code("FOOTY-1002")
                .message("Request Payload Format Invalid")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EmptyResourceCollectionException.class)
    public ResponseEntity<WarningMessage> handleNoResourcesPresent(EmptyResourceCollectionException exception) {
        var warning = exception.getLocalizedMessage();

        WarningMessage warningMessage = WarningMessage.builder()
                .timestamp(LocalDateTime.now())
                .code("FOOTY-200")
                .message(warning)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(warningMessage);

    }
}
