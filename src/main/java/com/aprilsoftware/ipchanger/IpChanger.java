package com.aprilsoftware.ipchanger;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupRulesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupRulesResponse;
import software.amazon.awssdk.services.ec2.model.RevokeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.Filter;

public class IpChanger
{
    private final IpCConfig config;

    public IpChanger(IpCConfig config)
    {
        this.config = config;
    }

    public void changeIp(String ip)
    {
        for (IpCSecurityGroup securityGroup : config.getSecurityGroups())
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

            for (IpCPermission permission : securityGroup.getPermissions())
            {
                AuthorizeSecurityGroupIngressRequest authRequest;

                authRequest = AuthorizeSecurityGroupIngressRequest.builder()
                    .groupId(securityGroup.getGroupId())
                    .ipPermissions(permission.toIpPermission(ip))
                    .build();

                ec2.authorizeSecurityGroupIngress(authRequest);
            }
        }
    }
}
