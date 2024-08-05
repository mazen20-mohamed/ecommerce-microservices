package com.example.SaleService.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(){

    }
    public NotFoundException(String message){
        super(message);
    }
}
