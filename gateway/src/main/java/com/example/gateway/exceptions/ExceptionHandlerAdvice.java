package com.example.gateway.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler({NullPointerException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;// 500

        return new ResponseEntity<>(new ErrorResponse(status,e.getMessage()),status);
    }

    @ExceptionHandler(NotAuthenticateException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthenticateException(Exception e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(new ErrorResponse(status,e.getMessage()),status);
    }

}
