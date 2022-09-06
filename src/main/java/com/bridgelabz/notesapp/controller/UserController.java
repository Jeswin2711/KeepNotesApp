package com.bridgelabz.notesapp.controller;

import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.model.Users;
import com.bridgelabz.notesapp.service.UserService;
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
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody Users users)
    {
        return userService.registerUser(users);
    }

    @GetMapping("/confirm-email")
    public String confirmUser(@RequestParam("token")String confirmationToken)
    {
        return userService.confirmEmail(confirmationToken);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto userloginDto) throws Exception {
        return userService.login(userloginDto);
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto ,
                                @PathVariable int id) throws Exception {
        return userService.resetPassword(id , resetPasswordDto);
    }

    @PostMapping("/forgot-password/{id}")
    public String forgotPassword(@PathVariable int id) throws Exception
    {
        return userService.forgotPassword(id);
    }

    @GetMapping("/getall")
    public ResponseEntity<Response> getAllUser()
    {
        return new ResponseEntity<>(userService.getAllUser() , HttpStatus.OK);
    }

    @PostMapping("/addnote/{id}")
    public ResponseEntity<Response> addNoteById(int id , @PathVariable List<Notes> notes) throws Exception
    {
        return new ResponseEntity<>(userService.addNoteById( id, notes ),HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnote/{note_id}")
    public ResponseEntity<Response> getNoteById(int user_id , int note_id) throws Exception
    {
        return new ResponseEntity<>(userService.getNoteById(user_id , note_id ) , HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/deletenote/{note_id}")
    public ResponseEntity<Response> deleteNoteById(int user_id , int note_id) throws Exception
    {
        return new ResponseEntity<>(userService.deleteNoteById(user_id,note_id),HttpStatus.OK);
    }

}

