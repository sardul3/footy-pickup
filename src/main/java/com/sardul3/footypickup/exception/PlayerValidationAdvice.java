package com.sardul3.footypickup.exception;

import com.sardul3.footypickup.domain.ErrorMessage;
import com.sardul3.footypickup.domain.WarningMessage;
import com.sardul3.footypickup.exception.custom.*;
import io.micrometer.core.annotation.Timed;
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
import java.util.stream.Stream;

@ControllerAdvice
@Slf4j
public class PlayerValidationAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    @Timed(value="errors.validation")
    public ResponseEntity<ErrorMessage> handleValidationErrors(WebExchangeBindException exception) {
        var errors = exception
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

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
    @Timed(value="errors.match.not.found")
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
    @Timed(value="errors.invalid.teams.in.match")
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
    @Timed(value="errors.invalid.request.format")
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

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @Timed(value="errors.duplicate.resource.found")
    public ResponseEntity<ErrorMessage> handleDuplicateResourceKey(ResourceAlreadyExistsException exception) {
        var errors = List.of(exception
                .getLocalizedMessage());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .code("FOOTY-1003")
                .message("Requested resource already exists, please create a new one")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EmptyResourceCollectionException.class)
    @Timed(value="errors.resource.not.found")
    public ResponseEntity<WarningMessage> handleNoResourcesPresent(EmptyResourceCollectionException exception) {
        var warning = exception.getLocalizedMessage();

        WarningMessage warningMessage = WarningMessage.builder()
                .timestamp(LocalDateTime.now())
                .code("FOOTY-2000")
                .message(warning)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(warningMessage);
    }

    @ExceptionHandler(TeamDoesNotHaveMinimumNumberOfPlayersException.class)
    @Timed(value="errors.invalid.number.of.players")
    public ResponseEntity<WarningMessage> handleUnbalancedPlayersInTeams(TeamDoesNotHaveMinimumNumberOfPlayersException exception) {
        var warning = exception.getLocalizedMessage();

        WarningMessage warningMessage = WarningMessage.builder()
                .timestamp(LocalDateTime.now())
                .code("FOOTY-2001")
                .message(warning)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(warningMessage);
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    @Timed(value="errors.player.already.exists")
    public ResponseEntity<WarningMessage> handlePlayerAlreadyExistsInTeam(PlayerAlreadyExistsException exception) {
        var warning = exception.getLocalizedMessage();

        WarningMessage warningMessage = WarningMessage.builder()
                .timestamp(LocalDateTime.now())
                .code("FOOTY-2002")
                .message(warning)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(warningMessage);
    }
}
