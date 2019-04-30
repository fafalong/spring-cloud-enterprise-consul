FROM java:8
VOLUME /tmp
ADD ./target/consul-gateway.jar /app.jar
ADD ./startup.sh /startup.sh

EXPOSE 8900

RUN chmod +x /startup.sh
ENTRYPOINT [ "/startup.sh" ]

#docker build -t spring-cloud/consul-gateway .
#docker run -d -p 8900:8900 --name consul-gateway -e REDIS_SERVER=192.168.3.19 -e AUTH_SERVER=auth-service -e CONSUL_SERVER=192.168.28.8 spring-cloud/consul-gateway
