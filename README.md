# Based on Spring Cloud Consul - micro services architecture  

# Maven Projects
1. consul-auth-service: OAuth2 Authentification, registered to Consul
2. consul-common: commonly used components by all other projects.
3. consul-gateway:  load-balanced configurations for svca and svcb, rate-limiter and etc.
4. consul-service-a: demonstrated how to use feign client and ribbon client with Oauth2 supported to call B project.
5. consul-service-b: 



# stacks points
* Spring boot 2.0.4
* Consul Server 1.4.4
* Hystrix & Dashboard
* Spring Cloud Sleuth/zipkin  
* Ribbon 
* Turbine 
* Feign
* Spring Cloud OAuth2 

