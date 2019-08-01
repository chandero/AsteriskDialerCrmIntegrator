// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CommonUtils.java

package utils;


public class CommonUtils
{

    public CommonUtils()
    {
    }

    public static String getFormattedUniqueId(String rawUniqueId)
    {
        String idParts[] = rawUniqueId.split("\\.");
        return idParts[0];
    }
}
