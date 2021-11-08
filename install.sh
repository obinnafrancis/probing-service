mv -f /etc/localtime /etc/localtime.bak
ln -s /usr/share/zoneinfo/Africa/Lagos /etc/localtime

set -e
cd /opt/probing-service/

java -jar -Dspring.profile.active=docker probing-service.jar
#