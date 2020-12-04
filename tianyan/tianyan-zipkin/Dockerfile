FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-zipkin

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-zipkin

EXPOSE 1025

ADD ./tianyan-zipkin/target/tianyan-zipkin.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-zipkin.jar
