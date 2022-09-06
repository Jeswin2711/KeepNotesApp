package com.bridgelabz.notesapp.service;

import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.utility.UserMailSender;
import com.bridgelabz.notesapp.model.ConfirmationToken;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.model.Users;
import com.bridgelabz.notesapp.repository.NotesRepository;
import com.bridgelabz.notesapp.repository.TokenRepository;
import com.bridgelabz.notesapp.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMailSender mailSender;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${fromEmail}")
    private String fromEmail;

    public String registerUser(Users users)
    {
        tokenRepository.save(new ConfirmationToken(String.valueOf(UUID.randomUUID()),users));
        userRepository.save(users);
        mailSender.confirmEmail(fromEmail,users.getEmail(),tokenRepository.getById(users.getId()).getToken());
        return "User Registered Successfully";
    }

    public String login(UserLoginDto userLoginDto) throws Exception {
        if(userRepository.findByUserName(userLoginDto.getUserName()).isPresent())
        {
            userRepository.findByPassWord(userLoginDto.getPassWord())
                    .ifPresent(
                            user ->
                            {
                                System.out.println("User Login Successfull");
                            }
                    );
        }
        else
        {
            throw new Exception("Invalid Credentials");
        }
        return "Login Successfull";
    }


    public String resetPassword(int id , ResetPasswordDto resetPasswordDto) throws Exception {
        if(userRepository.findById(id).isPresent())
        {
            if(userRepository.findById(id).get()
                    .getPassWord().equals(
                            resetPasswordDto.getOldPassword()
                    ))
            {
                userRepository.findById(id).get().setPassWord(resetPasswordDto.getNewPassword());
                mailSender.sendEmail(fromEmail,userRepository.findById(id).get().getEmail());
            }
            else
            {
                throw new Exception("Old password invalid");
            }
        }
        else
        {
            throw new Exception("ID not found");
        }
        return "Password Reset Successfull";
    }


    public String forgotPassword (int id) throws Exception {
        String toEmail = userRepository.findById(id).get().getEmail();

        if(userRepository.findById(id).isPresent())
        {
            mailSender.sendEmail(
                    fromEmail , toEmail
            );
        }
        else
        {
            throw new Exception("Invalid ID");
        }
        return "Mail Sent Successfully";
    }

    public Response getAllUser()
    {
        return new Response(
                "Users List",userRepository.findAll()
        );
    }

    public Response addNoteById(int id , List<Notes> notes) throws Exception {
        if(userRepository.findById(id).isPresent())
        {
           userRepository.findById(id).get().setNotes(notes);
           notes.addAll(notes);
        }
        else
        {
            throw new Exception("User ID not found");
        }

        return new Response("Note Added Successfully",HttpStatus.OK);
    }

    public Response getNoteById(int user_id ,int note_id) throws Exception {
        boolean flag = false;
        if(userRepository.findById(user_id).isPresent())
        {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for(Notes note : userNotes)
            {
                if (note.getId() == note_id)
                {
                    flag = true;
                }
            }
            if (!false)
            {
                System.out.println("Note ID Present");
            }
            else
            {
                throw new Exception("NOTE ID Not Found");
            }
        }
        else
        {
            throw new Exception("Invalid User Id");
        }
        return new Response("Note ID : "+ note_id , notesRepository.getById(note_id));
    }


    public Response deleteNoteById(int user_id , int note_id ) throws Exception {
        if(userRepository.findById(user_id).isPresent())
        {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for(Notes note : userNotes)
            {
                if (note.getId() == note_id)
                {
                    notesRepository.deleteById(note_id);
                }
            }
        }
        else
        {
            throw new Exception("Invalid User Id");
        }
        return new Response("Deleted Successfully Note ID : "+ note_id , HttpStatus.OK);
    }


    public String confirmEmail(String confirmationToken)
    {
        userRepository.getById(tokenRepository.findByToken(confirmationToken).get().getId()).setVerified(true);
        userRepository.save(userRepository.getById(tokenRepository.findByToken(confirmationToken).get().getId()));
        return "Verified Successfully";
    }
}
