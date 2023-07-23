package com.aprilsoftware.ipchanger;

public class IpCApp 
{
    public IpCApp()
    {
    }

    public static void main( String[] args )
    {
        new IpCService(args).startAndWait();
    }
}
