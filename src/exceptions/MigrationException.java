// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MigrationException.java

package exceptions;


public class MigrationException extends Exception
{

    public MigrationException()
    {
    }

    public MigrationException(String message)
    {
        super(message);
    }

    public MigrationException(Throwable cause)
    {
        super(cause);
    }

    public MigrationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
