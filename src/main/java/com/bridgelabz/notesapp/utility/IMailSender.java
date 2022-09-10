package com.bridgelabz.notesapp.utility;


/*
    Interface for Sending Mail
 */
public interface IMailSender
{
    void sendEmail(String from , String to);
    String confirmEmail(String from , String to , String token) throws Exception;

    void loginEmail(String from , String to , String token) throws Exception;
}
