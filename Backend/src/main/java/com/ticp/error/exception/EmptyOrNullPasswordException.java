package com.ticp.error.exception;

public class EmptyOrNullPasswordException extends Exception
{
    public EmptyOrNullPasswordException()
    {
        super();
    }

    public EmptyOrNullPasswordException(String message)
    {
        super(message);
    }

    public EmptyOrNullPasswordException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
