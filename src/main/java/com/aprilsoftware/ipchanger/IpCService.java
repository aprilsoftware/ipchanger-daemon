/*
 * Copyright 2025 April Software
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
    private IpDiscovery ipDiscovery;
    private IpChanger ipChanger;
    private String currentIp;
    private boolean verbose;

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

            verbose = config.isVerbose();
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

                verbose = config.isVerbose();

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
            if (verbose)
            {
                LOGGER.log(Level.INFO, "Checking new IP...");
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

            if (verbose)
            {
                LOGGER.log(Level.INFO, "IP Checked");
            }
        }
    }

    private void changeIp()
    {
        String newIp;

        newIp = ipDiscovery.discoverIp();

        if (currentIp == null)
        {
            ipChanger.changeIp(newIp);

            currentIp = newIp;

            LOGGER.log(Level.INFO, "Current IP " + currentIp);
        }
        else
        {
            if (!newIp.equals(currentIp))
            {
                ipChanger.changeIp(newIp);

                currentIp = newIp;

                LOGGER.log(Level.INFO, "IP changed to " + currentIp);
            }
        }
    }
}
