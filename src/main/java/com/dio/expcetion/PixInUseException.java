package com.dio.expcetion;

public class PixInUseException extends RuntimeException{
    public PixInUseException(final String message){
        super(message);
    }

}
