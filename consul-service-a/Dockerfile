FROM java:8
VOLUME /tmp
ADD ./target/consul-service-a.jar /app.jar
ADD ./startup.sh /startup.sh

EXPOSE 8901

RUN chmod +x /startup.sh
ENTRYPOINT [ "/startup.sh" ]

#docker build -t spring-cloud/consul-service-a .
#docker run -d -p 8901:8901 --name consul-service-a -e AUTH_SERVER=auth-service -e CONSUL_SERVER=192.168.28.8 spring-cloud/consul-service-a
