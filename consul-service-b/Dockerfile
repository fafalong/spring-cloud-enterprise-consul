FROM java:8
VOLUME /tmp
ADD ./target/consul-service-b.jar /app.jar
ADD ./startup.sh /startup.sh

EXPOSE 8902

RUN chmod +x /startup.sh
ENTRYPOINT [ "/startup.sh" ]

#docker build -t spring-cloud/consul-service-b .
#docker run -d -p 8902:8902 --name consul-service-b  -e AUTH_SERVER=auth-service -e CONSUL_SERVER=192.168.28.8 spring-cloud/consul-service-b
