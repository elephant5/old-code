FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-config

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-config

EXPOSE 1025

ADD ./tianyan-config/target/tianyan-config.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-config.jar
