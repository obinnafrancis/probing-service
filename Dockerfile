FROM openjdk:11

MAINTAINER Obinna Onwuakagba

EXPOSE 8069 8069

COPY install.sh /root/probing-service/install.sh
COPY setup.sh /root/probing-service/setup.sh

RUN chmod +x /root/probing-service/setup.sh
RUN /root/probing-service/setup.sh

COPY src/main/resources/application-docker.properties /opt/probing-service/application-docker.properties
COPY src/main/resources/banner.txt /opt/probing-service/banner.txt

ADD target/probing-service.tar.gz /opt/probing-service
WORKDIR /opt/probing-service

RUN chmod +x /root/probing-service/install.sh
RUN /root/probing-service/install.sh
#