package com.bridgelabz.notesapp.utility;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Response Model
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/*
 A Model for Response
 */
public class Response
{
    private String message;

    private Object data;
}
