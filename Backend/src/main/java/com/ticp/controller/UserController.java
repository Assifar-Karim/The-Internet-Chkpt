package com.ticp.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.error.exception.*;
import com.ticp.event.RegistrationCompleteEvent;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import com.ticp.util.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
public class UserController
{
    private static Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Registration Endpoints

    @PostMapping("/users")
    public String registerUser(@Valid @RequestBody UserDTO userDTO, final HttpServletRequest request) throws DuplicateUserException
    {
        User user = userService.registerUser(userDTO);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Registration";
    }
    @GetMapping("/verifications")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token)
    {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid"))
        {
            return ResponseEntity.ok("Verification Complete");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    @GetMapping("/verifications/regen")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request)
            throws TokenNotFoundException, MessagingException
    {
        VerificationToken verificationToken = userService.regenerateVerificationToken(oldToken);
        String url = applicationUrl(request) + "/verifications?token=" + verificationToken.getToken();
        emailSenderService.sendMessageUsingThymeleafTemplate(
                verificationToken.getUser().getEmail(),
                "The Internet Checkpoint Account Verification",
                Map.of("salutation","Welcome " + verificationToken.getUser().getUsername() + ",",
                        "verificationLink", url),
                "verification-email.html"
        );
        return ResponseEntity.ok("Verification token regenerated");
    }

    @PostMapping("/users/passwords/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request)
            throws EmptyOrNullEmailException, EmailNotFoundException, MessagingException
    {
        if(passwordDTO.getEmail() == null || passwordDTO.getEmail().isEmpty())
        {
            throw new EmptyOrNullEmailException("Please input a valid email");
        }
        PasswordToken passwordToken = userService.generatePasswordToken(passwordDTO);
        String url = applicationUrl(request) + "/users/passwords?token=" + passwordToken.getToken();
        emailSenderService.sendMessageUsingThymeleafTemplate(
                passwordToken.getUser().getEmail(),
                "The Internet Checkpoint Account Password Reset",
                Map.of("salutation","Hello " + passwordToken.getUser().getUsername() + ",",
                        "verificationLink", url),
                "password-reset-email.html"
        );
        return ResponseEntity.ok("Password reset token generated");
    }
    @GetMapping("/users/passwords/verifications")
    public ResponseEntity<?> verifyPasswordToken(@RequestParam("token") String token)
    {
        String result = userService.validatePasswordToken(token);
        if(result.equalsIgnoreCase("valid"))
        {
            return ResponseEntity.ok("Verification Complete");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    @PostMapping("/users/passwords")
    public ResponseEntity<?> savePassword(@RequestBody PasswordDTO passwordDTO)
            throws EmptyOrNullTokenException, EmptyOrNullPasswordException, EmailNotFoundException, TokenNotFoundException
    {
        if(passwordDTO.getToken() == null || passwordDTO.getToken().isEmpty())
        {
            throw new EmptyOrNullTokenException("Please input a valid token");
        }
        if(passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().isEmpty())
        {
            throw new EmptyOrNullPasswordException("Please input a valid password");
        }
        userService.changePassword(passwordDTO);
        return ResponseEntity.ok("Password Reset Successfully");
    }

    @GetMapping("jwt")
    public void refreshJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            try
            {
                DecodedJWT decodedJWT = jwtTokenUtil.verifyToken(authorizationHeader);
                String username = decodedJWT.getSubject();
                User user = userService.findUserByUsername(username);
                String accessToken = jwtTokenUtil.generateJwtToken(user.getUsername(), List.of(user.getRole()),request);
                jwtTokenUtil.issueTokenResponse(accessToken, authorizationHeader.substring("Bearer ".length()), response);
            }
            catch (Exception e)
            {
                jwtTokenUtil.issueTokenVerificationErrorResponse(response, e.getMessage());
            }
        }
        else
        {
            logger.error("Refresh token is missing");
            throw new RuntimeException("Refresh token is missing");
        }
    }

    // Internal class methods
    private String applicationUrl(HttpServletRequest request)
    {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
