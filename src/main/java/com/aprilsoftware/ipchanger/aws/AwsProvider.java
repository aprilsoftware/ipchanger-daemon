package com.aprilsoftware.ipchanger.aws;

import java.util.ArrayList;
import java.util.List;

import com.aprilsoftware.ipchanger.IpCProvider;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupRulesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupRulesResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.RevokeSecurityGroupIngressRequest;

public class AwsProvider implements IpCProvider
{
    private List<AwsSecurityGroup> securityGroups;

    public AwsProvider()
    {
        securityGroups = new ArrayList<>();
    }

    public List<AwsSecurityGroup> getSecurityGroups()
    {
        return securityGroups;
    }

    public void setSecurityGroups(List<AwsSecurityGroup> securityGroups)
    {
        this.securityGroups = securityGroups;
    }

    public void changeIp(String newIp)
    {
        for (AwsSecurityGroup securityGroup : securityGroups)
        {
            DescribeSecurityGroupRulesRequest describeRulesRequest;
            DescribeSecurityGroupRulesResponse describeRulesResponse;
            Ec2Client ec2;

            ec2 = Ec2Client.builder()
                .region(Region.of(securityGroup.getRegion()))
                .build();

            describeRulesRequest = DescribeSecurityGroupRulesRequest.builder()
                .filters(Filter.builder()
                    .name("group-id")
                    .values(securityGroup.getGroupId())
                    .build())
                .build();

            describeRulesResponse = ec2.describeSecurityGroupRules(describeRulesRequest);

            describeRulesResponse.securityGroupRules().forEach(rule ->
            {
                if (!rule.isEgress())
                {
                    RevokeSecurityGroupIngressRequest revokeRequest;

                    revokeRequest = RevokeSecurityGroupIngressRequest.builder()
                        .groupId(securityGroup.getGroupId())
                        .securityGroupRuleIds(rule.securityGroupRuleId())
                        .build();

                    ec2.revokeSecurityGroupIngress(revokeRequest);
                }
            });

            for (AwsPermission permission : securityGroup.getPermissions())
            {
                AuthorizeSecurityGroupIngressRequest authRequest;

                authRequest = AuthorizeSecurityGroupIngressRequest.builder()
                    .groupId(securityGroup.getGroupId())
                    .ipPermissions(permission.toIpPermission(newIp))
                    .build();

                ec2.authorizeSecurityGroupIngress(authRequest);
            }
        }
    }
}
