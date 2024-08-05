package com.example.gateway.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){

    }
    public BadRequestException(String message){
        super(message);
    }
}
