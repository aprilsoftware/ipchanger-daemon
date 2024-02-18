if [ "$(getent passwd ipchanger)" = "" ]; then
    useradd --home-dir /etc/ipchanger --system ipchanger
fi

if [ "$(stat --format '%U' /usr/local/ipchanger)" != 'ipchanger' ]; then
    chown -R ipchanger:ipchanger /usr/local/ipchanger
fi

if [ "$(stat --format '%U' /var/log/ipchanger)" != 'ipchanger' ]; then
    chown -R ipchanger:ipchanger /var/log/ipchanger
fi

if [ ! -f /etc/ipchanger/ipchanger.json ]; then
    cp /usr/share/ipchanger/ipchanger.json /etc/ipchanger/ipchanger.json
fi

if [ ! -f /etc/ipchanger/logging.properties ]; then
    cp /usr/share/ipchanger/logging.properties /etc/ipchanger/logging.properties
fi

if [ ! -f /etc/ipchanger/.aws/credentials ]; then
    cp /usr/share/ipchanger/credentials /etc/ipchanger/.aws/credentials
fi
