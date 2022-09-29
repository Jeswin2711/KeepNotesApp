package com.bridgelabz.notesapp.user.service;

import com.bridgelabz.notesapp.utility.security.UserCredentials;
import com.bridgelabz.notesapp.user.dto.ForgotPasswordDto;
import com.bridgelabz.notesapp.user.dto.ResetPasswordDto;
import com.bridgelabz.notesapp.user.dto.UserLoginDto;
import com.bridgelabz.notesapp.user.dto.UserRegisterDto;
import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.utility.security.JwtService;
import com.bridgelabz.notesapp.utility.UserMailSender;
import com.bridgelabz.notesapp.user.model.User;
import com.bridgelabz.notesapp.user.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.util.Random;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMailSender mailSender;

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

    public Response registerUser(UserRegisterDto userRegisterDto) {
        User user = mapper.map(userRegisterDto, User.class);
        userRepository.findByUserName(userRegisterDto.getUserName()).ifPresent(
                action -> {
                    try {
                        throw new CustomException("Username Already Taken");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        userRepository.findByEmail(userRegisterDto.getUserName()).ifPresent(
                action -> {
                    try {
                        throw new CustomException("Email Already Taken");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        userCredentials.setUserName(userRegisterDto.getUserName());
        userCredentials.setPassWord(userRegisterDto.getPassWord());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCredentials.getUserName(),
                        userCredentials.getPassWord()));

        UserDetails userDetails = userCredentials.loadUserByUsername(
                userRegisterDto.getUserName());

        String jwtToken = jwtService.generateToken(userDetails);

        userRepository.save(user);
        mailSender.confirmEmail(fromEmail, user.getEmail(), jwtToken);
        return new Response("User Registered Successfully", HttpStatus.OK);
    }

    public Response login(UserLoginDto userLoginDto) throws Exception {
        if (userRepository.findByUserName(userLoginDto.getUserName()).get().isVerified() == true) {
            if (userRepository.findByUserName(userLoginDto.getUserName()).isPresent() &&
                    userRepository.findByUserName(userLoginDto.getUserName()).get().getPassWord()
                            .equals(userLoginDto.getPassWord())) {
                userCredentials.setUserName(userLoginDto.getUserName());
                userCredentials.setPassWord(userLoginDto.getPassWord());

                UserDetails loginDetails = userCredentials.loadUserByUsername(userLoginDto.getUserName());
                String token = jwtService.generateToken(loginDetails);
                mailSender.loginEmail(fromEmail,
                        userRepository.findByUserName(userLoginDto.getUserName()).get().getEmail(),
                        token);
                return new Response(token, HttpStatus.OK);
            } else {
                throw new CustomException("Username or Password or Invalid");
            }
        } else {
            throw new CustomException("Please Verify you Email");
        }
    }

    public Response resetPassword(int user_id, ResetPasswordDto resetPasswordDto) throws CustomException {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if (userRepository.findById(user_id).get().getUserName().equals(userName)) {
            userRepository.findById(user_id).get().setPassWord(resetPasswordDto.getPassWord());
            userRepository.save(userRepository.findById(user_id).get());
        } else {
            throw new CustomException("Username Token not Matching");
        }
        return new Response("Password Reset Successfull", HttpStatus.OK);
    }

    public Response forgotPassword(String email, ForgotPasswordDto resetPasswordDto) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            if (resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
                userRepository.findByEmail(email).get().setPassWord(resetPasswordDto.getNewPassword());
                userRepository.save(userRepository.findByEmail(email).get());
                mailSender.sendEmail(fromEmail, email);
            } else {
                throw new CustomException("Password Not Matching");
            }
        } else {
            throw new CustomException("Email not found");
        }
        return new Response("Password Reset Successfull", HttpStatus.OK);
    }

    public Response confirmEmail(String confirmationToken) {
        String username = jwtService.extractUsername(confirmationToken);
        userRepository.findByUserName(username).get().setVerified(true);
        userRepository.save(userRepository.findByUserName(username).get());
        return new Response("Verified Successfully", HttpStatus.OK);
    }

    public int getUserId(String username) {
        return userRepository.findByUserName(username).get().getId();
    }

    public Response sendOtp(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            String otp = String.format("%06d", number);
            mailSender.sendOtp(email, otp);
            return new Response(otp, HttpStatus.OK);
        } else {
            throw new CustomException("Mail ID Not Found");
        }
    }
}
