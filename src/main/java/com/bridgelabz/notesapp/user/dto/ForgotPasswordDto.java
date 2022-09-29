package com.bridgelabz.notesapp.user.dto;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String newPassword;

    private String confirmPassword;
}
