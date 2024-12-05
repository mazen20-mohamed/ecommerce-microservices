package com.example.SaleService.exceptions;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(){

    }

    public UnAuthorizeException(String message){
        super(message);
    }
}
