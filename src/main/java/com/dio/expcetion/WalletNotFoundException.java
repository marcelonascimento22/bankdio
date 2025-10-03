package com.dio.expcetion;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException (final String message){
        super(message);
    }

}
