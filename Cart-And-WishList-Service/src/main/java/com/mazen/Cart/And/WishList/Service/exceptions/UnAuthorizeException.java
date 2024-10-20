package com.mazen.Cart.And.WishList.Service.exceptions;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(){

    }

    public UnAuthorizeException(String message){
        super(message);
    }
}
