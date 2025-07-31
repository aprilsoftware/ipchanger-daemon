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
