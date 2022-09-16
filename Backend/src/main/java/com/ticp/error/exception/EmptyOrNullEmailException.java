package com.ticp.error.exception;

public class EmptyOrNullEmailException extends Exception
{
    public EmptyOrNullEmailException()
    {
        super();
    }

    public EmptyOrNullEmailException(String message)
    {
        super(message);
    }

    public EmptyOrNullEmailException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
