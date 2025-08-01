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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aprilsoftware.ipchanger.IpCException;
import com.aprilsoftware.ipchanger.IpCProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OvhProvider extends IpCProvider
{
    private String endpoint;
    private String appKey;
    private String appSecret;
    private String consumerKey;
    private String ip;
    private List<OvhFirewallRule> firewallRules;

    public OvhProvider()
    {
        firewallRules = new ArrayList<>();
    }

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getConsumerKey()
    {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey)
    {
        this.consumerKey = consumerKey;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public List<OvhFirewallRule> getFirewallRules()
    {
        return firewallRules;
    }

    public void setFirewallRules(List<OvhFirewallRule> firewallRules)
    {
        this.firewallRules = firewallRules;
    }

    public void changeIp(String newIp)
    {
        OvhClient client;

        client = new OvhClient(endpoint,
                appKey,
                appSecret,
                consumerKey);

        for (OvhFirewallRule rule : firewallRules)
        {
            Map<String,Object> ruleMap;
            String path;
            String body;

            path = "/ip/" + ip + "/firewall/" + ip + "/rule";

            deletePreviousRule(rule, path, client);

            ruleMap = Map.of(
                "sequence", rule.getSequence(),
                "action", rule.getAction(),
                "protocol", rule.getProtocol(),
                "destinationPort", String.valueOf(rule.getDestinationPort()),
                "source", newIp + "/32");

            try
            {
                body = new ObjectMapper().writeValueAsString(ruleMap);
            }
            catch (Exception e)
            {
                throw new IpCException(e);
            }

            client.post(path, body);
        }
    }

    private void deletePreviousRule(OvhFirewallRule rule, String path, OvhClient client)
    {
        client.delete(path + "/" + rule.getSequence());

        for (int i = 0; i < 100; i++)
        {
            String response;

            try
            {
                Thread.sleep(10 * 1000);
            }
            catch (Exception e)
            {
            }

            response = client.get(path + "/" + rule.getSequence());

            if (response == null)
            {
                break;
            }
        }
    }
}
