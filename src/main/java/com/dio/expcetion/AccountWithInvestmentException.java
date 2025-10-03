package com.dio.expcetion;

public class AccountWithInvestmentException extends RuntimeException{
    public AccountWithInvestmentException(final String message){
        super(message);
    }

}
