package com.aprilsoftware.ipchanger.aws;

import java.util.ArrayList;
import java.util.List;

public class AwsSecurityGroup
{
    private String groupId;
    private String region;
    private List<AwsPermission> permissions;

    public AwsSecurityGroup()
    {
        permissions = new ArrayList<>();
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public List<AwsPermission> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(List<AwsPermission> permissions)
    {
        this.permissions = permissions;
    }
}
