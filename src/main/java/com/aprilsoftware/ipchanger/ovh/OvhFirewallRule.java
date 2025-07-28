package com.aprilsoftware.ipchanger.ovh;

public class OvhFirewallRule
{
    private int sequence;
    private String action;
    private String protocol;
    private int sourcePort;
    private int destinationPort;

    public OvhFirewallRule()
    {
    }

    public int getSequence()
    {
        return sequence;
    }

    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public int getSourcePort()
    {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort)
    {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort()
    {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort)
    {
        this.destinationPort = destinationPort;
    }
}
