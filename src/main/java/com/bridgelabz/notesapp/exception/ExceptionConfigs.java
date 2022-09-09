package com.bridgelabz.notesapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionConfigs
{
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionModel> handleCustomException(CustomException exception)
    {
        ExceptionModel message = new ExceptionModel(
                exception.getMessage(),
                new Date(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

}
