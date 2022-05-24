package com.ticp.controller;

import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.event.RegistrationCompleteEvent;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController
{
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ApplicationEventPublisher publisher;
    // Registration Endpoints

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO, final HttpServletRequest request)
    {
        User user = userService.registerUser(userDTO);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success";
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token)
    {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid"))
        {
            return "Verification Complete";
        }
        return result;
    }
    @GetMapping("/regenerateVerification")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request)
    {
        VerificationToken verificationToken = userService.regenerateVerificationToken(oldToken);
        if(verificationToken == null)
        {
            return "Non existing verification token";
        }
        String url = applicationUrl(request) + "/verify?token=" + verificationToken.getToken();
        emailSenderService.sendSimpleMail(
                verificationToken.getUser().getEmail(),
                "Click the following link to become an official main character : { " + url + "}",
                "The Internet Checkpoint Account Verification"
        );
        // NOTE (KARIM) : Replace with a log
        System.out.println("Click the following link to become an official main character : { " + url + "}");
        return "Token Regenerated";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request)
    {
        // This has to be changed with an exception
        if(passwordDTO.getEmail() == null)
        {
            return "Please Input an email";
        }
        PasswordToken passwordToken = userService.generatePasswordToken(passwordDTO);
        if(passwordToken == null)
        {
            return "Incorrect email";
        }
        String url = applicationUrl(request) + "/savePassword?token=" + passwordToken.getToken();
        emailSenderService.sendSimpleMail(
                passwordToken.getUser().getEmail(),
                "Click the following link to reset your TICP password : { " + url + "}",
                "The Internet Checkpoint Password Reset"
        );
        System.out.println("Click the following link to become an official main character : { " + url + "}");
        return "Token Generated";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordDTO passwordDTO)
    {
        if(passwordDTO.getNewPassword().equalsIgnoreCase("") || passwordDTO.getNewPassword() == null)
        {
            return "Please input a new password";
        }
        String result = userService.validatePasswordToken(token);
        if(!result.equalsIgnoreCase("valid"))
        {
            return result;
        }
        User user = userService.getUserByPasswordToken(token);
        if(user != null)
        {
            userService.changePassword(user, passwordDTO);
            return "Password Reset Successfully";
        }
        else
        {
            return "Invalid token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordDTO passwordDTO)
    {
        if(passwordDTO.getEmail() == null || passwordDTO.getOldPassword() == null || passwordDTO.getNewPassword() == null
            || passwordDTO.getNewPassword().equals(""))
        {
            return "Please input all of the necessary information";
        }
        User user = userService.findUserByEmail(passwordDTO.getEmail());
        if(user == null)
        {
            return "User not found";
        }
        if(!userService.verifyOldPassword(user, passwordDTO.getOldPassword()))
        {
            return "Invalid old password";
        }
        userService.changePassword(user, passwordDTO);
        return "Password Changed Successfully";

    }

    // Internal class methods
    private String applicationUrl(HttpServletRequest request)
    {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
