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
