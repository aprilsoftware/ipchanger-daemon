package com.aprilsoftware.ipchanger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IpCService
{
    private static final Logger LOGGER = Logger.getLogger(IpCService.class.getName());
    private final String[] args;
    private Timer timer;
    private boolean processing;
    private IpDiscovery ipDiscovery;
    private IpChanger ipChanger;
    private String currentIp;

    public IpCService(String[] args)
    {
        this.args = args;
    }

    public boolean isStarted()
    {
        if (timer == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void reloadConfig()
    {
        if (timer != null &&
            ipDiscovery != null &&
            ipChanger != null)
        {
            IpCConfig config;

            config = IpCConfig.load(args[0]);

            ipDiscovery = new IpDiscovery(config);

            ipChanger = new IpChanger(config);
        }
    }

    public void start()
    {
        if (timer == null)
        {
            IpCConfig config;

            if (args.length != 1)
            {
                throw new IllegalArgumentException("Invalid number of arguments");
            }
            else
            {
                config = IpCConfig.load(args[0]);
            }

            timer = new Timer();

            if (config.getFrequency() > 0)
            {
                ipDiscovery = new IpDiscovery(config);

                ipChanger = new IpChanger(config);

                timer.schedule(new IpChangerTask(), config.getDelay(), config.getFrequency());
            }
        }
    }

    public void startAndWait()
    {
        try
        {
            this.start();

            synchronized(this)
            {
                this.wait();
            }
        }
        catch (Throwable t)
        {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
            
            System.exit(1);
        }
    }
    
    public void stop()
    {
        if (timer != null)
        {
            try
            {
                timer.cancel();
            }
            catch (Throwable t)
            {
                LOGGER.log(Level.SEVERE, t.getMessage(), t);
            }

            timer = null;

            processing = false;

            ipDiscovery = null;

            ipChanger = null;

            currentIp = null;
        }
    }

    private class IpChangerTask extends TimerTask
    {
        public IpChangerTask()
        {
        }
        
        @Override
        public void run()
        {
            synchronized(this)
            {
                if (processing)
                {
                    return;
                }
                else
                {
                    processing = true;
                }
            }
            
            try
            {
                changeIp();
            }
            catch (Throwable t)
            {
                try
                {
                    LOGGER.log(Level.SEVERE, t.getMessage(), t);
                }
                catch (Throwable th)
                {
                }
            }
            finally
            {
                processing = false;
            }
        }
    }

    private void changeIp()
    {
        if (currentIp == null)
        {
            currentIp = ipDiscovery.discoverIp();

            ipChanger.changeIp(currentIp);

            LOGGER.log(Level.INFO, "Current IP " + currentIp);
        }
        else
        {
            String newIp;

            newIp = ipDiscovery.discoverIp();

            if (!newIp.equals(currentIp))
            {
                currentIp = newIp;

                ipChanger.changeIp(currentIp);

                LOGGER.log(Level.INFO, "IP changed to " + currentIp);
            }
        }
    }
}
