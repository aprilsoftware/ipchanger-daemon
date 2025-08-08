# ipchanger-daemon

*ipchanger-daemon* is a small Java service that keeps **remote‑access firewalls in sync with your current, dynamic WAN IP address**.

Originally written for AWS Security Groups, it now also supports the **OVHcloud Edge Network Firewall**—and even custom **local commands**—so you can protect EC2 instances, OVH VPS/dedicated servers, and trigger any local automation from the same daemon.

---

## Features

- **Multiple providers**:
  - `aws` – updates one or more security‑groups.
  - `ovh` – updates one or more edge‑firewall rules (per‑IP firewall).
  - `command` – runs a local shell command when the IP changes.
- **Polymorphic JSON config** – extend with new providers without touching the core daemon.
- **Runs as a foreground process, systemd service, or Apache ****\`\`**** daemon**.
- Java 11+

---

## Build

Java 11+, Maven and Git are required:

```bash
    git clone https://github.com/aprilsoftware/ipchanger-daemon.git
    cd ipchanger-daemon
    mvn package
```

`target/` will now contain the runnable `ipchanger-daemon.jar`.

---

## Credentials

### AWS

The AWS SDK looks for credentials in `~/.aws/credentials`. The simplest setup is the *default* profile:

```ini
[default]
aws_access_key_id = XXX
aws_secret_access_key = YYY
```

---

### OVHcloud

Create an **API Key**:

Rights:
```bash
GET: /ip/*/firewall/*/rule/*
POST: /ip/*/firewall/*/rule
DELETE: /ip/*/firewall/*/rule/*
```

---

### Command provider

No credentials are required. The daemon simply runs the configured command locally.
The command can include placeholders such as {ip} to inject the newly detected IP address.

Example:

```bash
echo "New IP is {newIp}" >> /var/log/ipchanges.log
```

---

## Configuration

Create `ipchanger.json` in the working directory. Below is a **minimal dual‑provider example**:

```jsonc
{
  "delay": 0,
  "frequency": 5000,
  "url": "https://api.ipify.org",
  "verbose": false,

  "providers": [
    {
      "type": "aws",
      "securityGroups": [
        {
          "groupId": "sg-xxxxxxxxxxxx",
          "region": "eu-central-1",
          "permissions": [
            { "ipProtocol": "tcp", "fromPort": 22, "toPort": 22 }
          ]
        }
      ],
      "postIpChangeCommand": ""         // optional
    },
    {
      "type": "ovh",
      "endpoint": "eu",
      "ip": "100.0.0.1",
      "firewallRules": [
        {
          "sequence": 0,                // 0‑19
          "action": "permit",
          "protocol": "tcp",
          "sourcePort": 0,              // optional
          "destinationPort": 22
        }
      ],
      "postIpChangeCommand": ""         // optional
    },
    {
      "type": "command",
      "ipChangeCommand": "echo 'My new IP is {newIp}'"
    }
  ]
}
```

---


## Running

```bash
java -jar target/ipchanger-daemon.jar ipchanger.json
```
---

## Run it as a daemon on Linux

You can run ipchanger-changer as a daemon using [jsvc](https://commons.apache.org/proper/commons-daemon/jsvc.html).

---

## Debian package

Download the latest Debian package from the [release page](https://github.com/aprilsoftware/ipchanger-daemon/releases).

---

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
