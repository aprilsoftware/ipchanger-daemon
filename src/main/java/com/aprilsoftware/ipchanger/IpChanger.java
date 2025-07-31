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
                LOGGER.log(Level.INFO, "Running Post-IP change command");

                runCommand(command);
            }
        }
    }

    private void runCommand(String command)
    {
        Process process;
        int exitCode;

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
