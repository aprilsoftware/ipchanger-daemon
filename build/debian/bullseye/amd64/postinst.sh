if [ ! id -u ipchanger > /dev/null 2>&1 ]; then
    useradd --home-dir /etc/ipchanger --system ipchanger
fi

if [ ! $(stat --format '%U' /usr/local/ipchanger) == 'ipchanger' ]; then
    chown -R ipchanger:ipchanger /usr/local/ipchanger
fi

if [ ! $(stat --format '%U' /var/log/ipchanger) == 'ipchanger' ]; then
    chown -R root:root /var/log/ipchanger
fi

mkdir -p /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64

if [ ! -L /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64/server ]; then
    ln -s /usr/lib/jvm/java-11-openjdk-amd64/lib/server /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64/
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
