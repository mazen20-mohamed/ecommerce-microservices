package com.mazen.ProductService.exceptions;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(){

    }

    public UnAuthorizeException(String message){
        super(message);
    }
}
