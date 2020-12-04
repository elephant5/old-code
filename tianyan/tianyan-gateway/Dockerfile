FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-gateway

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-gateway

EXPOSE 1025

ADD ./tianyan-gateway/target/tianyan-gateway.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-gateway.jar
