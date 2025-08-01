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
import java.net.HttpURLConnection;

public class IpDiscovery
{
    private IpCConfig config;

    public IpDiscovery(IpCConfig config)
    {
        this.config = config;
    }

    public String discoverIp()
    {
        HttpURLConnection connection;
        BufferedReader in;
        String inputLine;
        StringBuffer content;

        content = new StringBuffer();

        try
        {
            connection = (HttpURLConnection) config.getUrl().openConnection();

            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
    
            connection.setRequestMethod("GET");

            try
            {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                try
                {
                    while ((inputLine = in.readLine()) != null)
                    {
                        content.append(inputLine);
                    }
                }
                finally
                {
                    in.close();
                }
            }
            finally
            {
                connection.disconnect();
            }
        }
        catch (IOException e)
        {
            throw new IpCException(e);
        }

        return content.toString();
    }
}
