FROM java:8
VOLUME /tmp
ADD ./target/consul-auth-service.jar /app.jar
ADD ./startup.sh /startup.sh

EXPOSE 8001

RUN chmod +x /startup.sh
ENTRYPOINT [ "/startup.sh" ]

#docker build -t spring-cloud/consul-auth-service .
#docker run -d -p 8001:8001 --name consul-auth-service -e MYSQL_SERVER_PASSWORD=password -e CONSUL_SERVER=192.168.28.8 spring-cloud/consul-auth-service
