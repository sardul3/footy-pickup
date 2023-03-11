package com.sardul3.footypickup.exception.custom;

public class TeamDoesNotHaveMinimumNumberOfPlayersException extends RuntimeException{
    public TeamDoesNotHaveMinimumNumberOfPlayersException(String message) {
        super(message);
    }
}
