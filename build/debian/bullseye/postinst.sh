if ! id -u ipchanger > /dev/null 2>&1; then
    adduser --quiet --system --group --home /usr/local/ipchanger ipchanger

    chown -R ipchanger:ipchanger /usr/local/ipchanger

    chown -R ipchanger:ipchanger /var/log/ipchanger
fi

mkdir -p /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64

if [ ! -f /usr/lib/jvm/java-11-openjdk-amd64/lib/server ]; then
    ln -s /usr/lib/jvm/java-11-openjdk-amd64/lib/server /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64/
fi

if [ ! -f /etc/ipchanger/ipchanger.json ]; then
    cp /usr/share/ipchanger/ipchanger.json /etc/ipchanger/ipchanger.json
fi

if [ ! -f /usr/local/ipchanger/.aws/credentials ]; then
    cp /usr/share/ipchanger/credentials /usr/local/ipchanger/.aws/credentials
fi
