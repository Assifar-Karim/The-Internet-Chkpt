package com.ticp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Document("verification-tokens")
public class VerificationToken
{
    private static final int EXPIRATION_TIME_MIN = 10;
    @Id
    private String id;
    private String token;
    private Date expirationTime;
    User user;

    public VerificationToken()
    {}

    public VerificationToken(String token)
    {
        this.token = token;
    }

    public VerificationToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.expirationTime = computeExpirationDate(EXPIRATION_TIME_MIN);
    }

    private Date computeExpirationDate(int expirationTimeMin)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTimeMin);
        return new Date(calendar.getTime().getTime());
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Date getExpirationTime()
    {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime)
    {
        this.expirationTime = expirationTime;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
