package com.dio.expcetion;

public class NoFundsEnoughException extends RuntimeException {
    public NoFundsEnoughException (final String message){
        super(message);
    }

}
