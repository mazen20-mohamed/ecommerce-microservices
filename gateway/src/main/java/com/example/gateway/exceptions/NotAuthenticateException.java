package com.example.gateway.exceptions;

public class NotAuthenticateException extends RuntimeException{
    public NotAuthenticateException(){

    }
    public NotAuthenticateException(String msg){
        super(msg);
    }
}
