FROM anapsix/alpine-java:8_server-jre_unlimited



RUN mkdir -p /tianyan/bin/com.colourfulchina/tianyan-auth

WORKDIR /tianyan/bin/com.colourfulchina/tianyan-auth

EXPOSE 1025

ADD ./tianyan-auth/target/tianyan-auth.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar tianyan-auth.jar
