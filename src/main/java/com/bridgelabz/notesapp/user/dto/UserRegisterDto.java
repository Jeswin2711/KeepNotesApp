package com.bridgelabz.notesapp.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto
{
    private String userName;

    private String passWord;

    private String email;

    private long phoneNumber;
}
