package com.bridgelabz.notesapp.controller;

import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.service.UserServiceImpl;
import com.bridgelabz.notesapp.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController
{
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegisterDto userRegisterDto)
    {
        return userServiceImpl.registerUser(userRegisterDto);
    }

    @GetMapping("/confirm-email/{token}")
    public String confirmUser(@PathVariable String token)
    {
        return userServiceImpl.confirmEmail(token);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto userloginDto) throws CustomException {
        return userServiceImpl.login(userloginDto);
    }

    @PutMapping("/reset-password/{email}")
    public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto ,
                                @PathVariable String email) throws CustomException {
        return userServiceImpl.resetPassword(email , resetPasswordDto);
    }

    @PostMapping("/forgot-password/{email}")
    public String forgotPassword(@PathVariable String email) throws CustomException
    {
        return userServiceImpl.forgotPassword(email);
    }

    @PostMapping("/addnote/{id}")
    public ResponseEntity<Response> addNoteById(@PathVariable int id , @RequestBody Notes notes) throws CustomException
    {
        return new ResponseEntity<>(userServiceImpl.addNoteById( id, notes ),HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnote/{note_id}")
    public ResponseEntity<Response> getNoteById(@PathVariable int user_id , @PathVariable int note_id) throws CustomException
    {
        return new ResponseEntity<>(userServiceImpl.getNoteById(user_id , note_id ) , HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnotes")
    public ResponseEntity<Response> getNotesById(@PathVariable int user_id) throws CustomException
    {
        return new ResponseEntity<>(userServiceImpl.getNotesById(user_id) , HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/deletenote/{note_id}")
    public ResponseEntity<Response> deleteNoteById(@PathVariable int user_id , @PathVariable int note_id) throws CustomException
    {
        return new ResponseEntity<>(userServiceImpl.deleteNoteById(user_id,note_id),HttpStatus.OK);
    }
}

