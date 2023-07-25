# ipchanger-daemon

ipchanger-daemon is a deamon that automatically updates AWS security group rules with your current public dynamic IP address.

It could be helpfull when you want to access your EC2 VMs only from your dynamic public IP which by definition changes from time to time.

# Build

Java 11+, maven and git are required to build and compile the source.

    git clone https://github.com/aprilsoftware/ipchanger-daemon.git
    cd ipchanger-daemon
    mvn package

The target folder contains the ipchanger-daemon.jar executable and the required libraries.

# .aws/credentials

ipchanger-daemon uses the AWS API java library from Amazon which requires the credentials to be set using the credentials file in the .aws folder. The .aws folder must be located in the current folder of the application.

    [default]
    aws_access_key_id = xxx
    aws_secret_access_key = xxx

# Configure the security groups

Create a file named ipchanger.json (in the ipchanger-daemon folder)

    {
    	"delay": 0,
    	"frequency": 5000,
    	"url": "https://api.ipify.org",
    	"securityGroups": [
		    {
                "groupId": "sg-xxxxxxxxxxxxxx",
			    "region": "eu-central-1",
			    "permissions": [
    				{
					    "ipProtocol": "tcp",
					    "fromPort": 22,
					    "toPort": 22
				    }
			    ]
		    }	
	    ]
    }


# Run it

    java -jar target/ipchanger-daemon.jar ../ipchanger.json


# Run it as a daemon on Linux

You can run ipchanger-changer as a daemon using [jsvc](https://commons.apache.org/proper/commons-daemon/jsvc.html).


# Debian package

You can download the latest Debian package from the [release page](https://github.com/aprilsoftware/ipchanger-daemon/releases).

