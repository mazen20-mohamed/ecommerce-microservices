package com.mazen.OrderService.exceptions;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(){

    }

    public UnAuthorizeException(String message){
        super(message);
    }
}
