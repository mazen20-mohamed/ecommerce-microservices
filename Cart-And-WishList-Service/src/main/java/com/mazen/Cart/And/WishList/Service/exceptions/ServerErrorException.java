package com.mazen.Cart.And.WishList.Service.exceptions;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException(){

    }
    public ServerErrorException(String error){
        super(error);
    }
}
