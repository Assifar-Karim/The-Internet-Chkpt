package com.ticp.event;

import com.ticp.model.User;
import org.springframework.context.ApplicationEvent;

public class RegistrationCompleteEvent extends ApplicationEvent
{
    private User user;
    private String applicationUrl;
    public RegistrationCompleteEvent(User user, String applicationUrl)
    {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getApplicationUrl()
    {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl)
    {
        this.applicationUrl = applicationUrl;
    }
}
