package com.mazen.OrderService.exceptions;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException(){

    }
    public ServerErrorException(String error){
        super(error);
    }
}
