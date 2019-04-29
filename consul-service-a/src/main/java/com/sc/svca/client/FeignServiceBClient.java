package com.sc.svca.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "consul-service-b", fallback = FeignServiceBClient.ServiceBClientFallback.class)
public interface FeignServiceBClient {

    @GetMapping(value = "/test")
    String test();
    
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
    
    //String getSessionId(@RequestHeader("X-Auth-Token") String token);

    @Component
    class ServiceBClientFallback implements FeignServiceBClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBClientFallback.class);

        @Override
        public String test() {
            LOGGER.info("now flow into callback method...");
            return "SERVICE B FAILED! - FALLING BACK";
        }
        
        @Override
        public String sayHiFromClientOne(String name) {
        	return "SERVICE sayHiFromClientOne  FAILED! - FALLING BACK - with parameter - name:" + name;
        	//SERVICE sayHiFromClientOne  FAILED! - FALLING BACK - with parameter - name:abc
        	
        }
    }
}