package com.mazen.Cart.And.WishList.Service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        return new ResponseEntity<>(
                new ErrorResponse(status,e.getMessage()),
                status
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e){
        HttpStatus status = HttpStatus.BAD_REQUEST; // 404

        return new ResponseEntity<>(
                new ErrorResponse(status,e.getMessage()),
                status
        );
    }

    @ExceptionHandler({NullPointerException.class,ServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;// 500

        return new ResponseEntity<>(new ErrorResponse(status,e.getMessage()),status);
    }

    @ExceptionHandler({UnAuthorizeException.class,AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponse> handleUnAuthorizeException(Exception e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;// 401

        return new ResponseEntity<>(new ErrorResponse(status,e.getMessage()),status);
    }

}
