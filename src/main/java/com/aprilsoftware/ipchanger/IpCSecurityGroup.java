package com.aprilsoftware.ipchanger;

import java.util.ArrayList;
import java.util.List;

public class IpCSecurityGroup
{
    private String groupId;
    private String region;
    private List<IpCPermission> permissions;

    public IpCSecurityGroup()
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

    public List<IpCPermission> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(List<IpCPermission> permissions)
    {
        this.permissions = permissions;
    }
}
