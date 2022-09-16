package com.ticp.error.exception;

public class EmptyOrNullTokenException extends Exception
{
    public EmptyOrNullTokenException()
    {
        super();
    }

    public EmptyOrNullTokenException(String message)
    {
        super(message);
    }

    public EmptyOrNullTokenException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
