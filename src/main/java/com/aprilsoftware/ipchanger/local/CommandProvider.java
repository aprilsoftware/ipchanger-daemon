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
package com.aprilsoftware.ipchanger.local;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.aprilsoftware.ipchanger.IpCProvider;
import com.aprilsoftware.ipchanger.IpChanger;

public class CommandProvider extends IpCProvider
{
    private static final Logger LOGGER = Logger.getLogger(CommandProvider.class.getName());
    private String ipChangeCommand;

    public CommandProvider()
    {
    }

    public String getIpChangeCommand()
    {
        return ipChangeCommand;
    }

    public void setIpChangeCommand(String ipChangeCommand)
    {
        this.ipChangeCommand = ipChangeCommand;
    }

    public void changeIp(String newIp)
    {
        try
        {
            
            IpChanger.runCommand(ipChangeCommand.replace("{newIp}", newIp));
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
    }
}
