package com.aprilsoftware.ipchanger;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;

public class IpCDaemon implements Daemon
{
    private static final Logger LOGGER = Logger.getLogger(IpCDaemon.class.getName());
    private IpCService service;

    public IpCDaemon()
    {
    }

    @Override
    public void init(DaemonContext daemonContext)
    {
        try
        {
            if (service != null &&
                service.isStarted())
            {
                service.reloadConfig();
            }
            else
            {
                service = new IpCService(daemonContext.getArguments());
            }
        }
        catch (Throwable t)
        {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
        }
    }
    
    @Override
    public void start()
    {
        try
        {
            service.start();
        }
        catch (Throwable t)
        {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
        }
    }

    @Override
    public void stop()
    {
        try
        {
            service.stop();
        }
        catch (Throwable t)
        {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
        }
    }
    
    @Override
    public void destroy()
    {
        service = null;
    }
}
