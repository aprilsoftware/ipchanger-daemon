package com.aprilsoftware.ipchanger.aws;

import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.IpRange;

public class AwsPermission
{
    private String ipProtocol;
    private int fromPort;
    private int toPort;

    public AwsPermission()
    {
    }

    public String getIpProtocol()
    {
        return ipProtocol;
    }

    public void setIpProtocol(String ipProtocol)
    {
        this.ipProtocol = ipProtocol;
    }

    public int getFromPort()
    {
        return fromPort;
    }

    public void setFromPort(int fromPort)
    {
        this.fromPort = fromPort;
    }

    public int getToPort()
    {
        return toPort;
    }

    public void setToPort(int toPort)
    {
        this.toPort = toPort;
    }

    public IpPermission toIpPermission(String ip)
    {
        IpPermission ipPerm;
        IpRange ipRange;

        ipRange = IpRange.builder()
            .cidrIp(ip + "/32").build();

        ipPerm = IpPermission.builder()
            .ipProtocol(ipProtocol)
            .fromPort(fromPort)
            .toPort(toPort)
            .ipRanges(ipRange)
            .build();

        return ipPerm;
    }
}
