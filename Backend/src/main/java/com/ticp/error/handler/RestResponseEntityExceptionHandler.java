package com.ticp.error.handler;

import com.ticp.error.ErrorMessage;
import com.ticp.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorMessage> duplicateUserException(DuplicateUserException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorMessage> tokenNotFoundException(TokenNotFoundException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
    @ExceptionHandler(EmptyOrNullEmailException.class)
    public ResponseEntity<ErrorMessage> emptyOrNullEmailException(EmptyOrNullEmailException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
    @ExceptionHandler(EmptyOrNullPasswordException.class)
    public ResponseEntity<ErrorMessage> emptyOrNullPasswordException(EmptyOrNullPasswordException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
    @ExceptionHandler(EmptyOrNullTokenException.class)
    public ResponseEntity<ErrorMessage> emptyOrNullTokenException(EmptyOrNullTokenException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorMessage> emailNotFoundException(EmailNotFoundException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessage> IOException(IOException exception, WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
    }
}
