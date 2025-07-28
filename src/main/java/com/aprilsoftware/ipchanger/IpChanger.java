package com.aprilsoftware.ipchanger;

public class IpChanger
{
    private final IpCConfig config;

    public IpChanger(IpCConfig config)
    {
        this.config = config;
    }

    public void changeIp(String ip)
    {
        for (IpCProvider provider : config.getProviders())
        {
            provider.changeIp(ip);
        }
    }
}
