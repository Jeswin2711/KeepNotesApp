package com.bridgelabz.notesapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionModel
{
    private String msg;
    private Date timeStamp;
    private HttpStatus statusCode;
}
