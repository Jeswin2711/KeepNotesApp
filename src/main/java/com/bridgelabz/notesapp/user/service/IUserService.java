package com.bridgelabz.notesapp.user.service;

import com.bridgelabz.notesapp.user.dto.ForgotPasswordDto;
import com.bridgelabz.notesapp.user.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.user.dto.UserLoginDto;
import com.bridgelabz.notesapp.user.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.utility.Response;

public interface IUserService {
    Response registerUser(UserRegisterDto userRegisterDto);

    Response login(UserLoginDto userLoginDto) throws Exception;

    Response resetPassword(int user_id, ResetPasswordDto resetPasswordDto) throws CustomException;

    Response forgotPassword(String email, ForgotPasswordDto forgotPasswordDto) throws Exception;

    Response confirmEmail(String confirmationToken);
}
