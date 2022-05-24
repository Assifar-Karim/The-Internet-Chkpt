package com.ticp.event.listener;

import com.ticp.event.RegistrationCompleteEvent;
import com.ticp.model.User;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>
{
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event)
    {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        String url = event.getApplicationUrl() + "/verify?token=" + token;
        emailSenderService.sendSimpleMail(
                user.getEmail(),
                "Click the following link to become an official main character : { " + url + "}",
                "The Internet Checkpoint Account Verification"
        );
        System.out.println("Click the following link to become an official main character : { " + url + "}");
    }
}
