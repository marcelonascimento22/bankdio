package com.dio.expcetion;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException (final String message){
        super(message);
    }

}
