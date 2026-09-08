package com.pokearena.exception;

public class InvalidBattleException extends RuntimeException{
    public InvalidBattleException(String message){
        super(message);
    }
}
