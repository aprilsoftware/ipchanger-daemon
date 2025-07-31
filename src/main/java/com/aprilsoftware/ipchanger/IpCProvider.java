package com.aprilsoftware.ipchanger;

import com.aprilsoftware.ipchanger.aws.AwsProvider;
import com.aprilsoftware.ipchanger.ovh.OvhProvider;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AwsProvider.class, name = "aws"),
    @JsonSubTypes.Type(value = OvhProvider.class, name = "ovh")
})
public abstract class IpCProvider
{
    private String postIpChangeCommand;

    public IpCProvider()
    {
    }

    public String getPostIpChangeCommand()
    {
        return postIpChangeCommand;
    }

    public void setPostIpChangeCommand(String postIpChangeCommand)
    {
        this.postIpChangeCommand = postIpChangeCommand;
    }

    public abstract void changeIp(String newIp);
}
