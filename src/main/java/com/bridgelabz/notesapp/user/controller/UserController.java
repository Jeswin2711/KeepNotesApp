package com.bridgelabz.notesapp.user.controller;

import com.bridgelabz.notesapp.user.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.user.dto.UserLoginDto;
import com.bridgelabz.notesapp.user.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.user.service.UserServiceImpl;
import com.bridgelabz.notesapp.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController
{
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserRegisterDto userRegisterDto)
    {
        return new ResponseEntity<>(userServiceImpl.registerUser(userRegisterDto), HttpStatus.OK);
    }

    @GetMapping("/confirm-email/{token}")
    public ResponseEntity<Response> confirmUser(@PathVariable String token)
    {
        return new ResponseEntity<>(userServiceImpl.confirmEmail(token),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody UserLoginDto userloginDto) throws Exception {
        return new ResponseEntity<>(userServiceImpl.login(userloginDto),HttpStatus.OK);
    }

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto ,
                                @PathVariable String email) throws CustomException {
        return new ResponseEntity<>(userServiceImpl.resetPassword(email , resetPasswordDto),HttpStatus.OK);
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<Response> forgotPassword(@PathVariable String email) throws CustomException , Exception
    {
        return new ResponseEntity<>(userServiceImpl.forgotPassword(email),HttpStatus.OK);
    }

}

