package com.sc.svcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import com.sc.cmn.config.FeignHystrixConcurrencyStrategy;

@EnableTurbine
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableOAuth2Client
@EnableCircuitBreaker //Note: 如果不加，Dashboard无法接收到来自Feign内部断路器的监控数据
@EnableHystrix //Note: 开启断路器
//@EnableHystrixDashboard //Hystrix仪表板, server UI part, now using standalone version
@ComponentScan({"com.sc.cmn","com.sc.svcb"})
public class ServiceBApplication {
	
	 /**
	  	* 访问地址 http://localhost:8762/actuator/hystrix.stream
	  	* @param args
     */

    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
    }
    

    @Bean
    public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
        return new FeignHystrixConcurrencyStrategy();
    } 

}