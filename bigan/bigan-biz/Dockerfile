FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-upms

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-upms

EXPOSE 1025

ADD ./tianyan-upms/tianyan-upms-biz/target/tianyan-upms-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-upms-biz.jar
