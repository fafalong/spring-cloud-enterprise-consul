package com.sc.svca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.sc.cmn.config.FeignHystrixConcurrencyStrategy;

@EnableTurbine //Notes: 开启turbine，@EnableTurbine注解包含了@EnableDiscoveryClient注解
//@EnableTurbineStream # 不可共存
@EnableDiscoveryClient 
@EnableFeignClients
@SpringBootApplication
@EnableCircuitBreaker //Note: 如果不加，Dashboard无法接收到来自Feign内部断路器的监控数据
@EnableHystrix //Note: 开启断路器
//@EnableHystrixDashboard //Hystrix仪表板, server UI part, now using standalone version
@EnableOAuth2Client
@ComponentScan({"com.sc.cmn","com.sc.svca"})
public class ServiceAApplication {
	
	 /**
	  * http://localhost:8764/turbine.stream
	  	* 访问地址 http://localhost:8762/actuator/hystrix.stream
	  	* @param args
     */

    public static void main(String[] args) {
        SpringApplication.run(ServiceAApplication.class, args);
    }
    
 
    @Bean
    public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
        return new FeignHystrixConcurrencyStrategy();
    } 
    
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
    

}