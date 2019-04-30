package com.sc.auth.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc.auth.entity.OAuthUser;
import com.sc.auth.entity.User;

@RestController
@RequestMapping("/")
public class UserController {

    @GetMapping(value = "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }
    
    @Autowired
    private TokenStore tokenStore;
 
    //[root@hadoopnode4 ~]# curl -X POST -i -H "Authorization: Bearer ed83ed7b-b5c3-49db-b28e-a55f84a686b0" http://192.168.28.8:8001/uaa/getCurrUserId
    //@PostMapping("/bar")
    @GetMapping("/getCurrUserId")
    public String bar(@RequestHeader("Authorization") String auth) {
 
    	OAuthUser user = (OAuthUser) tokenStore.readAuthentication(auth.split(" ")[1]).getPrincipal();
 
        //User user = userDetails.getUser();
 
        return user.getUsername(); // + ":" + user.getPassword();
    } 
    
}


/**
		[root@hadoopnode4 ~]# curl -i -H "Authorization: Bearer fd69ea49-0b8a-457f-ae7b-73cdd42bff4d" http://192.168.1.63:5000/current
				 {
				    "authorities": [
				        {
				            "authority": "ROLE_ADMIN"
				        },
				        {
				            "authority": "ROLE_USER"
				        }
				    ],
				    "details": {
				        "remoteAddress": "192.168.1.63",
				        "sessionId": "79393902E56F6DB115F64F98A61D9BBD",
				        "tokenValue": "fd69ea49-0b8a-457f-ae7b-73cdd42bff4d",
				        "tokenType": "Bearer",
				        "decodedDetails": null
				    },
				    "authenticated": true,
				    "userAuthentication": {
				        "authorities": [
				            {
				                "authority": "ROLE_ADMIN"
				            },
				            {
				                "authority": "ROLE_USER"
				            }
				        ],
				        "details": {
				            "grant_type": "password",
				            "username": "admin",
				            "scope": "read write"
				        },
				        "authenticated": true,
				        "principal": {
				            "password": null,
				            "username": "admin",
				            "authorities": [
				                {
				                    "authority": "ROLE_ADMIN"
				                },
				                {
				                    "authority": "ROLE_USER"
				                }
				            ],
				            "accountNonExpired": true,
				            "accountNonLocked": true,
				            "credentialsNonExpired": true,
				            "enabled": true,
				            "user": null
				        },
				        "credentials": null,
				        "name": "admin"
				    },
				    "credentials": "",
				    "principal": {
				        "password": null,
				        "username": "admin",
				        "authorities": [
				            {
				                "authority": "ROLE_ADMIN"
				            },
				            {
				                "authority": "ROLE_USER"
				            }
				        ],
				        "accountNonExpired": true,
				        "accountNonLocked": true,
				        "credentialsNonExpired": true,
				        "enabled": true,
				        "user": null
				    },
				    "oauth2Request": {
				        "clientId": "client",
				        "scope": [
				            "read",
				            "write"
				        ],
				        "requestParameters": {
				            "grant_type": "password",
				            "scope": "read write",
				            "username": "admin"
				        },
				        "resourceIds": [],
				        "authorities": [],
				        "approved": true,
				        "refresh": false,
				        "redirectUri": null,
				        "responseTypes": [],
				        "extensions": {},
				        "grantType": "password",
				        "refreshTokenRequest": null
				    },
				    "clientOnly": false,
				    "name": "admin"
				}
*/