package com.ticp.error.exception;

public class TokenNotFoundException extends Exception
{
    public TokenNotFoundException()
    {
        super();
    }

    public TokenNotFoundException(String message)
    {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
