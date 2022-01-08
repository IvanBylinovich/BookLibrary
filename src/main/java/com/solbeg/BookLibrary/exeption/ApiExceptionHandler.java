package com.solbeg.BookLibrary.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e){
        HttpStatus bedRequest = HttpStatus.BAD_REQUEST;
        ApiExceptionInfo apiExceptionInfo = new ApiExceptionInfo(
                e.getMessage(),
                bedRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionInfo, bedRequest);

    }
}
