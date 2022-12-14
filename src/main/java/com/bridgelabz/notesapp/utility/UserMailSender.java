package com.bridgelabz.notesapp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class UserMailSender implements IMailSender
{

    @Autowired
    private JavaMailSender javaMailSender;


    public String confirmEmail(String from , String to , String token)
    {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject("Mail Form Admin");
            messageHelper.setText("<html><body>" + " <p>To Verify Email Click " +
                    "<a href = http://localhost:8080/user/confirm-email/"+token+">here</a></p></body></html>",true);
            javaMailSender.send(mimeMessage);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "Kindly Verify Email";
    }

    private String messageBuilder(String email)
    {
        return "<html><head>Hi User,</head><body>Password Request Successfull for Email"+email+"</body></html>";
    }


    public void loginEmail(String from , String to , String token) throws Exception {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject("Mail Form Admin");
            messageHelper.setText("<html><body>You are Successfully Login with token : "+ token +"</body></html>",true);
            javaMailSender.send(mimeMessage);
        }
        catch (Exception e)
        {
            throw new Exception("Error when Logging in");
        }
    }


    public void forgotEmail(String fromEmail , String toEmail , String password) throws Exception {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Mail Form Admin");
            messageHelper.setText("<html><body>Password Reset Successfully. User Account password"+password +"If you want to Change this password Kindly reset Password</body></html>",true);
            javaMailSender.send(mimeMessage);
        }
        catch (Exception e)
        {
            throw new Exception("Error when Restting password");
        }
    }

    @Override
    public void sendEmail(String from, String to)
    {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject("Mail Form Admin");
            messageHelper.setText(messageBuilder(to),true);
            javaMailSender.send(mimeMessage);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
