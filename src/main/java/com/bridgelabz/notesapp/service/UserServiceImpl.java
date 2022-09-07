package com.bridgelabz.notesapp.service;

import com.bridgelabz.notesapp.utility.security.UserCredentials;
import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.utility.security.JwtService;
import com.bridgelabz.notesapp.service.interfaces.UserService;
import com.bridgelabz.notesapp.utility.UserMailSender;
import com.bridgelabz.notesapp.model.ConfirmationToken;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.model.Users;
import com.bridgelabz.notesapp.repository.NotesRepository;
import com.bridgelabz.notesapp.repository.TokenRepository;
import com.bridgelabz.notesapp.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMailSender mailSender;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserCredentials userCredentials;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${fromEmail}")
    private String fromEmail;

    public String registerUser(UserRegisterDto userRegisterDto)
    {
        Users user = mapper.map(userRegisterDto , Users.class);
        userRepository.findByUserName(userRegisterDto.getUserName()).ifPresent(
                action -> {
                    try {
                        throw new CustomException("Username Already Taken");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        userRepository.findByEmail(userRegisterDto.getUserName()).ifPresent(
                action -> {
                    try {
                            throw new CustomException("Username Already Taken");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }}
        );
        userCredentials.setUserName(userRegisterDto.getUserName());
        userCredentials.setPassWord(userRegisterDto.getPassWord());


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCredentials.getUserName(),
                        userCredentials.getPassWord()
                )
        );

        UserDetails userDetails = userCredentials.loadUserByUsername(
                userRegisterDto.getUserName()
        );

        String jwtToken = jwtService.generateToken(userDetails);


        tokenRepository.save(new ConfirmationToken(jwtToken,user));
        userRepository.save(user);
        mailSender.confirmEmail(fromEmail,user.getEmail(),tokenRepository.getById(user.getId()).getToken());
        return "User Registered Successfully";
    }

    public String login(UserLoginDto userLoginDto) throws CustomException {
        if(userRepository.findByUserName(userLoginDto.getUserName()).get().isVerified() == true)
        {
            if(userRepository.findByUserName(userLoginDto.getUserName()).isPresent() &&
            userRepository.findByUserName(userLoginDto.getUserName()).get().getPassWord().equals(userLoginDto.getPassWord()))
            {
                return "Login Successfull";
            }
            else
            {
                return "Username or Password or Invalid";
            }
        }
        else
        {
            throw new CustomException("Please Verify you Email");
        }
    }


    public String resetPassword(int id , ResetPasswordDto resetPasswordDto) throws CustomException {
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
                throw new CustomException("Old password invalid");
            }
        }
        else
        {
            throw new CustomException("ID not found");
        }
        return "Password Reset Successfull";
    }


    public String forgotPassword (int id) throws CustomException {
        String toEmail = userRepository.findById(id).get().getEmail();

        if(userRepository.findById(id).isPresent())
        {
            mailSender.sendEmail(
                    fromEmail , toEmail
            );
        }
        else
        {
            throw new CustomException("Invalid ID");
        }
        return "Mail Sent Successfully";
    }

    public Response addNoteById(int id , List<Notes> notes) throws CustomException {
        if(userRepository.findById(id).isPresent())
        {
           userRepository.findById(id).get().setNotes(notes);
           notes.addAll(notes);
           notesRepository.saveAll(notes);
        }
        else
        {
            throw new CustomException("User ID not found");
        }

        return new Response("Note Added Successfully",HttpStatus.OK);
    }

    public Response getNoteById(int user_id ,int note_id) throws CustomException {
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
                throw new CustomException("NOTE ID Not Found");
            }
        }
        else
        {
            throw new CustomException("Invalid User Id");
        }
        return new Response("Note ID : "+ note_id , notesRepository.getById(note_id));
    }


    public Response getNotesById(int user_id) throws CustomException {
        List<Notes> userNotes;
        if(userRepository.findById(user_id).isPresent())
        {
            userNotes = userRepository.findById(user_id).get().getNotes();
        }
        else
        {
            throw new CustomException("Invalid User Id");
        }
        return new Response("User Id : "+ user_id , userNotes);
    }


    public Response deleteNoteById(int user_id , int note_id ) throws CustomException {
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
            throw new CustomException("Invalid User Id");
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
