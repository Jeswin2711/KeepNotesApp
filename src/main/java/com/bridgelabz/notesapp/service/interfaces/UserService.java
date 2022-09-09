package com.bridgelabz.notesapp.service.interfaces;

import com.bridgelabz.notesapp.dto.NotesDto;
import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.utility.Response;

import java.util.List;

public interface UserService
{
    String registerUser(UserRegisterDto userRegisterDto);
    String login(UserLoginDto userLoginDto) throws CustomException;
    String resetPassword(String email , ResetPasswordDto resetPasswordDto) throws CustomException;
     String forgotPassword (String email) throws CustomException;

     Response addNoteById(int id , NotesDto notes) throws CustomException;
    Response getNoteById(int user_id ,int note_id) throws CustomException;
    Response deleteNoteById(int user_id , int note_id ) throws CustomException;
    String confirmEmail(String confirmationToken);
}
