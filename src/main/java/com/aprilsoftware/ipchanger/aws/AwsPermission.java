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
