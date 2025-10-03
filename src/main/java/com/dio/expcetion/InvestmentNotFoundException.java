package com.dio.expcetion;

public class InvestmentNotFoundException extends RuntimeException {
    public InvestmentNotFoundException (final String message){
        super(message);
    }

}
