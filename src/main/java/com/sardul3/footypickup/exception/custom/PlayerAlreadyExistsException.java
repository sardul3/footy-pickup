package com.sardul3.footypickup.exception.custom;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
