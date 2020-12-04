FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-monitor

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-monitor

EXPOSE 1025

ADD ./tianyan-monitor/target/tianyan-monitor.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-monitor.jar
