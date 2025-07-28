package com.aprilsoftware.ipchanger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IpCConfig
{
    private List<IpCProvider> providers;
    private long delay;
    private long frequency;
    private URL url;
    private boolean verbose;

    public IpCConfig()
    {
        providers = new ArrayList<>();
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

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    public boolean isVerbose()
    {
        return verbose;
    }

    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

    public List<IpCProvider> getProviders()
    {
        return providers;
    }

    public void setProviders(List<IpCProvider> providers)
    {
        this.providers = providers;
    }

    public static IpCConfig load(String configFilePath)
    {
        try
        {
            return new ObjectMapper().readValue(Files
                    .readString(Paths.get(configFilePath)), IpCConfig.class);
        }
        catch (Exception e)
        {
            throw new IpCException(e);
        }
    }
}
