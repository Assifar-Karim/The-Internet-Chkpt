package com.ticp.error.exception;

public class EmailNotFoundException extends Exception
{
    public EmailNotFoundException()
    {
        super();
    }

    public EmailNotFoundException(String message)
    {
        super(message);
    }

    public EmailNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
