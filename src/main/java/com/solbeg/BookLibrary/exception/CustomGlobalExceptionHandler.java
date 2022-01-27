package com.solbeg.BookLibrary.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            AuthorNotFoundByIdException.class,
            AuthorAlreadyExistByFirstNameAndLastNameException.class
    })
    public ResponseEntity<Object> handleAuthorException(RuntimeException exception) {
        return createResponseEntityFromRuntimeException(exception);
    }

    @ExceptionHandler({
            BookNotFoundByIdException.class,
            BookAlreadyExistByTitleAndAuthorException.class
    })
    public ResponseEntity<Object> handleBookException(RuntimeException exception) {
        return createResponseEntityFromRuntimeException(exception);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        CustomErrorResponseForMethodArgumentNotValidException argumentNotValidException = new CustomErrorResponseForMethodArgumentNotValidException(
                ZonedDateTime.now(),
                status.value(),
                errors
        );
        return new ResponseEntity<>(argumentNotValidException, headers, status);
    }

    private ResponseEntity<Object> createResponseEntityFromRuntimeException(RuntimeException exception) {
        CustomErrorResponseForRuntimeException error = new CustomErrorResponseForRuntimeException(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
