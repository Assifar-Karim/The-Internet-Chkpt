package com.ticp.event.listener;

import com.ticp.event.RegistrationCompleteEvent;
import com.ticp.model.User;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;
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
        String url = event.getApplicationUrl() + "/verifications?token=" + token;
        Map<String, Object> templateModel = Map.of(
                "salutation","Welcome " + user.getUsername() + ",",
                "verificationLink", url);
        try
        {
            emailSenderService.sendMessageUsingThymeleafTemplate(
                    user.getEmail(),
                    "The Internet Checkpoint Account Verification",
                    templateModel,
                    "verification-email.html");
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
