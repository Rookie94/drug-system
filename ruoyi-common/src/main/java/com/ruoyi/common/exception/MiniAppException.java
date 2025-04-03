package com.ruoyi.common.exception;

public class MiniAppException extends Exception
{
    public MiniAppException()
    {
    }

    public MiniAppException(String message)
    {
        super(message);
    }

    public MiniAppException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MiniAppException(Throwable cause)
    {
        super(cause);
    }

    public MiniAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
