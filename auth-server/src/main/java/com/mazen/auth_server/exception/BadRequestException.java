package com.mazen.auth_server.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){

    }
    public BadRequestException(String message){
        super(message);
    }
}
