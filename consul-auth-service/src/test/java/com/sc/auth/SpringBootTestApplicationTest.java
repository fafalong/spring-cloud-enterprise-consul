package com.sc.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =     SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void token_password() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "admin");
        params.add("password", "password");
        //params.add("scope", "scope1 scope2");
        String response = restTemplate.withBasicAuth("client", "secret").
                postForObject("/oauth/token", params, String.class);
        System.out.println("================pwd===============>"+ response);
    }

    @Test
    public void token_client() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        String response = restTemplate.withBasicAuth("service-b", "password").
                postForObject("/oauth/token", params, String.class);
        System.out.println("===============================>"+ response);
    }
    
    
    
    
}