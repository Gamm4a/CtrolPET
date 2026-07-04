package com.example.ctrolpet.exception;

public class UnauthorizedActionException extends RuntimeException{

    public UnauthorizedActionException(String message){
        super(message);
    }


}
