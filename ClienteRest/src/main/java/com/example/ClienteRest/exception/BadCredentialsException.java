package com.example.ClienteRest.exception;

public class BadCredentialsException extends  RuntimeException {

    public BadCredentialsException(String message){
        super(message);
    }

}
