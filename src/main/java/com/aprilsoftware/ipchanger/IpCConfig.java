package com.aprilsoftware.ipchanger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IpCConfig
{
    private List<IpCSecurityGroup> securityGroups;
    private long delay;
    private long frequency;
    private URL url;

    public IpCConfig()
    {
        securityGroups = new ArrayList<>();
    }

    public long getDelay()
    {
        return delay;
    }

    public void setDelay(long delay)
    {
        this.delay = delay;
    }

    public long getFrequency()
    {
        return frequency;
    }

    public void setFrequency(long frequency)
    {
        this.frequency = frequency;
    }

    public URL getURL()
    {
        return url;
    }

    public void setURL(URL url)
    {
        this.url = url;
    }

    public List<IpCSecurityGroup> getSecurityGroups()
    {
        return securityGroups;
    }

    public static IpCConfig load(String configFilePath)
    {
        String config;

        try
        {
            config = new String(Files.readAllBytes(Paths.get(configFilePath)));

            return new ObjectMapper().readValue(config, IpCConfig.class);
        }
        catch (Exception e)
        {
            throw new IpCException(e);
        }
    }
}
