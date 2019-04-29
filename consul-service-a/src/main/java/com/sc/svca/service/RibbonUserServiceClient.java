package com.sc.svca.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sc.cmn.config.RestTemplateClient;

 

/**
   RestTemplate(Oauth2 supported)+Ribbon 
 **/
@Service
public class RibbonUserServiceClient extends RestTemplateClient {

    //@Autowired
    //RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "userIdNotFound")
    public String getCurrUserId(HttpServletRequest request) {

    	String userId = getRestObject("consul-auth-service", "/getCurrUserId", getHeaders(request), null, String.class);
 
        return userId;
    }

    public String userIdNotFound(HttpServletRequest request) {
        return "can not find any userId logged in!";
    } 
}