package com.bridgelabz.notesapp.user.dto;

import lombok.Data;

@Data
public class ResetPasswordDto
{
    private String newPassword;

    private String confirmPassword;
}
