package com.aprilsoftware.ipchanger;

public class IpCException extends RuntimeException
{
    public IpCException(String message)
    {
        super(message);
    }

    public IpCException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IpCException(Throwable cause)
    {
        super(cause);
    }
}
