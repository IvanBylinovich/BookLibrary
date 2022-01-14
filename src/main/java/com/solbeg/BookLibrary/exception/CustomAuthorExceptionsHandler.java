package com.solbeg.BookLibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomAuthorExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthorNotFoundException.class, AuthorAlreadyExistException.class})
    public ResponseEntity<Object> handleAuthorExceptions(RuntimeException exception) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(ZonedDateTime.now());
        errors.setError(exception.getMessage());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errors);
    }
}
