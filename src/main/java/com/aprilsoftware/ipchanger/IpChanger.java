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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IpChanger
{
    private static final Logger LOGGER = Logger.getLogger(IpChanger.class.getName());

    private final IpCConfig config;

    public IpChanger(IpCConfig config)
    {
        this.config = config;
    }

    public void changeIp(String ip)
    {
        for (IpCProvider provider : config.getProviders())
        {
            String command;

            provider.changeIp(ip);

            command = provider.getPostIpChangeCommand();

            if (command != null && !command.isBlank())
            {
                runCommand(command);
            }
        }
    }

    private void runCommand(String command)
    {
        Process process;
        int exitCode;

        LOGGER.log(Level.INFO, "Running Post-IP change command");

        try
        {
            process = new ProcessBuilder("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())))
                {
                    StringBuilder sb;
                    String output;
                    String line;

                    sb = new StringBuilder();

                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        sb.append("\n");
                    }

                    output = sb.toString();

                    if (!output.trim().isBlank())
                    {
                        LOGGER.log(Level.INFO, output.toString());
                    }
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }).start();

            exitCode = process.waitFor();

            if (exitCode == 0)
            {
                LOGGER.log(Level.INFO, "Post-IP change command successfully executed");
            }
            else
            {
                LOGGER.log(Level.SEVERE, "Post-IP change command failed with exit code " + exitCode);
            }
        }
        catch (IOException | InterruptedException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
