package com.bridgelabz.notesapp.service;

import com.bridgelabz.notesapp.utility.security.UserCredentials;
import com.bridgelabz.notesapp.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.dto.UserLoginDto;
import com.bridgelabz.notesapp.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.utility.security.JwtService;
import com.bridgelabz.notesapp.service.interfaces.UserService;
import com.bridgelabz.notesapp.utility.UserMailSender;
import com.bridgelabz.notesapp.model.Notes;
import com.bridgelabz.notesapp.model.User;
import com.bridgelabz.notesapp.repository.NotesRepository;
import com.bridgelabz.notesapp.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private ModelMapper mapper;

    @Autowired
    private UserCredentials userCredentials;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletRequest httpServlet;

    @Autowired
    private JwtService jwtService;

    @Value("${fromEmail}")
    private String fromEmail;

    public String registerUser(UserRegisterDto userRegisterDto)
    {
        User user = mapper.map(userRegisterDto , User.class);
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

        userRepository.save(user);
        mailSender.confirmEmail(fromEmail,user.getEmail(),jwtToken);
        return "User Registered Successfully";
    }

    public String login(UserLoginDto userLoginDto) throws CustomException {
        if(userRepository.findByUserName(userLoginDto.getUserName()).get().isVerified() == true)
        {
            if(userRepository.findByUserName(userLoginDto.getUserName()).isPresent() &&
            userRepository.findByUserName(userLoginDto.getUserName()).get().getPassWord().equals(userLoginDto.getPassWord()))
            {
                UserDetails loginDetails = userCredentials.loadUserByUsername(userLoginDto.getUserName());
                String token = jwtService.generateToken(loginDetails);
                mailSender.loginEmail(fromEmail,userRepository.findByUserName(userLoginDto.getUserName()).get().getEmail(),
                        token);
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


    public String resetPassword(String email , ResetPasswordDto resetPasswordDto) throws CustomException {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if(userRepository.findByEmail(email).get().getUserName().equals(userName))
        {
            if(resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword()))
            {
                userRepository.findByEmail(email).get().setPassWord(resetPasswordDto.getNewPassword());
                userRepository.save(userRepository.findByEmail(email).get());
                mailSender.sendEmail(fromEmail,email);
            }
            else
            {
                throw new CustomException("Password Not Matching");
            }
        }
        else
        {
            throw new CustomException("ID not found");
        }
        return "Password Reset Successfull";
    }


    public String forgotPassword (String email) throws CustomException {
        String toEmail = userRepository.findByEmail(email).get().getEmail();

        if(userRepository.findByEmail(email).isPresent())
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

    public Response addNoteById(int id , Notes notes) throws CustomException {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if(userRepository.findById(id).get().getUserName().equals(userName)) {
            userRepository.findById(id).get().getNotes().add(notes);
            notesRepository.save(notes);
        }
        else
        {
            throw new CustomException("ID and Token Not Matching");
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
        String username = jwtService.extractUsername(confirmationToken);
        userRepository.findByUserName(username).get().setVerified(true);
        userRepository.save(userRepository.findByUserName(username).get());
        return "Verified Successfully";
    }
}
