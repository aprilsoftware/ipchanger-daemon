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
            connection = (HttpURLConnection) config.getURL().openConnection();

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
